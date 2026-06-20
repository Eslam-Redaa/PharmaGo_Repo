package com.demo.Serv_imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.demo.Entities.Order;
import com.demo.Entities.OrderItem;
import com.demo.Entities.Pharmacy;
import com.demo.Enums.OrderStatus;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.IO.AppResponse;
import com.demo.IO.InventoryRequest;
import com.demo.IO.OrderItemRequest;
import com.demo.IO.OrderRequest;
import com.demo.IO.PharmacyResponse;
import com.demo.Repositories.OrderRepository;
import com.demo.Repositories.PharmacyRepository;
import com.demo.Services.OrderServices;
import com.demo.Utils.AppUtils;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServImp implements OrderServices {

    @Autowired
    private PharmacyServImp pharmacyService;

    @Autowired
    private InventoryServImp inventoryService;

    @Autowired
    private AppUtils utils;

    @Autowired
    private PharmacyRepository pharmacyRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Override
    public AppResponse CreateOrder(OrderRequest request) {

        List<Long> nearbyPharmacyIds = pharmacyService
                .FindNearbyPharmacies(request.getUserLat(), request.getUserLong(), 25)
                .getPharmacyList()
                .stream()
                .map(PharmacyResponse::getId)
                .toList();

        if (nearbyPharmacyIds.isEmpty()) {
            throw new RuntimeException("No nearby pharmacies found");
        }

        Long selectedPharmacyId = findPharmacyWithAllItems(nearbyPharmacyIds, request.getOrderItems());

        if (selectedPharmacyId == null) {
            throw new RuntimeException("No pharmacy has all requested medicines in required quantities");
        }

        Order order = utils.ConvertOrderToEntity(request);

        order.setPharmacy(
                pharmacyRepo.findById(selectedPharmacyId)
                        .orElseThrow(() -> new RuntimeException("Pharmacy not found"))
        );

        double totalPrice = calculateTotalPrice(order);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepo.save(order);

        updateInventoryAfterOrder(selectedPharmacyId, request.getOrderItems());

        //utils.sendEmail(order.getUser().getEmail() , "Order Created",
        						//	"Your order has been created successfully");
        
        AppResponse response = new AppResponse();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setSuccessMessage("Order created successfully"
        		+ "-   OrderId : " + order.getId()
        		+ "-   PharmacyId : " + selectedPharmacyId);
        
        return response;
    }
    
    //---------------------------------------------------------------
    
    
    @Override
    public AppResponse CancelOrder(Long orderId, Long userId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to cancel this order");
        }

        if ( !order.getStatus().equals(OrderStatus.PENDING.toString()) ) {
            throw new RuntimeException("Only pending orders can be cancelled");
        }

        restoreInventory(order);

        order.setStatus(OrderStatus.CANCELLED.toString());

        orderRepo.save(order);

        AppResponse response = new AppResponse();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setSuccessMessage("Order Cancelled successfully");
        return response;
    }

    // ================= HELPER METHODS =================

    private Long findPharmacyWithAllItems(List<Long> pharmacyIds, List<OrderItemRequest> items) {

        for (Long pharmacyId : pharmacyIds) {

            boolean allAvailable = true;

            for (OrderItemRequest item : items) {
                boolean available = inventoryService.CheckMedicineAvailability(
                        pharmacyId,
                        item.getMedicineId(),
                        item.getQuantity()
                );

                if (!available) {
                    allAvailable = false;
                    break;
                }
            }

            if (allAvailable) {
                return pharmacyId;
            }
        }

        return null;
    }
    
    //--------------------------------------------------------

    private double calculateTotalPrice(Order order) {
        return order.getOrderItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
    
  //--------------------------------------------------------

    private void updateInventoryAfterOrder(Long pharmacyId, List<OrderItemRequest> items) {

        for (OrderItemRequest item : items) {

            var inventory = inventoryService
                    .GetInventoryByIds(pharmacyId, item.getMedicineId())
                    .getInventory();

            int newQuantity = inventory.getQuantity() - item.getQuantity();

            if (newQuantity < 0) {
                throw new RuntimeException("Inventory inconsistency detected");
            }

            inventoryService.UpdateInventory(
                    new InventoryRequest(
                            item.getMedicineId(),
                            pharmacyId,
                            newQuantity
                    )
            );
        }
    }
    
  //--------------------------------------------------------
    
    private void restoreInventory(Order order) {

        Long pharmacyId = order.getPharmacy().getId();

        for (OrderItem item : order.getOrderItems()) {

            var inventory = inventoryService
                    .GetInventoryByIds(pharmacyId, item.getMedicine().getId())
                    .getInventory();

            inventory.setQuantity(
                    inventory.getQuantity() + item.getQuantity()
            );

            inventoryService.UpdateInventory(
                    new InventoryRequest(
                            item.getMedicine().getId(),
                            pharmacyId,
                            inventory.getQuantity()
                    )
            );
        }
    }
    
    //------------------------------------------------

    @Override
    public AppResponse confirmOrder(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ( !order.getStatus().equals(OrderStatus.PENDING.toString()) ) {
            throw new RuntimeException(
                    "Only pending orders can be confirmed"
            );
        }

        order.setStatus(OrderStatus.CONFIRMED.toString());

        orderRepo.save(order);

        AppResponse response = new AppResponse();
        response.setStatusCode(HttpStatus.OK.value());
        response.setSuccessMessage("Order confirmed successfully");
        return response;
    }
    
    //------------------------------------------------

    @Override
    public AppResponse rejectOrder(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ( !order.getStatus().equals(OrderStatus.PENDING.toString()) ) {
            throw new RuntimeException(
                    "Only pending orders can be rejected"
            );
        }

        restoreInventory(order);

        order.setStatus(OrderStatus.REJECTED.toString());

        orderRepo.save(order);

        AppResponse response = new AppResponse();
        response.setStatusCode(HttpStatus.OK.value());
        response.setSuccessMessage("Order rejected successfully");
        return response;
    }

	@Override
	public AppResponse GetOrderById(Long orderId) {

		Order order = orderRepo.findById(orderId)
				.orElseThrow( () -> new ElementNotFoundException("Order with Id ( " + orderId + " ) Not Found"));
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setOrder( utils.ConvertOrderToResponse(order) );
		return response;
	}

	@Override
	public AppResponse GetAllOrders() {
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setOrderList( orderRepo.findAll().stream()
				.map( ord -> utils.ConvertOrderToResponse(ord))
				.collect(Collectors.toList()));
		return response;
	}

  
}