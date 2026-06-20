package com.demo.Utils;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.demo.Entities.Inventory;
import com.demo.Entities.Medicine;
import com.demo.Entities.Order;
import com.demo.Entities.OrderItem;
import com.demo.Entities.Pharmacy;
import com.demo.Entities.User;
import com.demo.Enums.OrderStatus;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.IO.InventoryDto;
import com.demo.IO.InventoryRequest;
import com.demo.IO.InventoryResponse;
import com.demo.IO.MedicineRequest;
import com.demo.IO.MedicineResponse;
import com.demo.IO.OrderDto;
import com.demo.IO.OrderItemRequest;
import com.demo.IO.OrderItemResponse;
import com.demo.IO.OrderRequest;
import com.demo.IO.OrderResponse;
import com.demo.IO.PharmacyRequest;
import com.demo.IO.PharmacyResponse;
import com.demo.IO.UserRequest;
import com.demo.IO.UserResponse;
import com.demo.Repositories.InventoryRepository;
import com.demo.Repositories.MedicineRepository;
import com.demo.Repositories.PharmacyRepository;
import com.demo.Repositories.UserRepository;
import com.demo.Serv_imp.MedicineServImp;

@Component
public class AppUtils {
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	InventoryRepository irepo;
	
	@Autowired
	MedicineRepository mrepo;
	
	@Autowired
	PharmacyRepository prepo;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	PasswordEncoder myPasswordEncoder;
	
	//@Autowired
	//MedicineServImp mserv;
	
	public Medicine ConvertMedicineToEntity(MedicineRequest request) {
		return Medicine.builder()
				.name(request.getName().toLowerCase())
				.description(request.getDescription())
				.category(request.getCategory())
				.price(request.getPrice())
				.requiresPrescription(request.isRequiresPrescription())
				.inventories(new ArrayList<>())
				.build();
	}
	
	public MedicineResponse ConvertMedicineToResponse(Medicine medicine) {
		return MedicineResponse.builder()
				.id(medicine.getId())
				.name(medicine.getName())
				.description(medicine.getDescription())
				.category(medicine.getCategory())
				.price(medicine.getPrice())
				.requiresPrescription(medicine.isRequiresPrescription())
				.imageUrl(medicine.getImageUrl())
				.build();
	}
	
	public InventoryDto ConvertMedicineToDto(Medicine medicine) {
		return InventoryDto.builder()
				.pmId(medicine.getId())
				.pmName(medicine.getName())
				.ImageUrl(medicine.getImageUrl())
				.build();
	}
	
	
	//____________________________________________________________________
	
	
	public Inventory ConvertInventoryToEntity(InventoryRequest request) {
		return Inventory.builder()
				.medicine(mrepo.findById(request.getMedicineId())
						.orElseThrow(  () -> new ElementNotFoundException("medicine with this id : " + request.getMedicineId() + " -> not found")) )
				.pharmacy(prepo.findById(request.getPharmacyId())
						.orElseThrow(  () -> new ElementNotFoundException("Pharmacy with this id : " + request.getMedicineId() + " -> not found")) )
				.quantity(request.getQuantity())
				.lastUpdated(Timestamp.valueOf(LocalDateTime.now()))
				.build();
	}
	
	public InventoryResponse ConvertInventoryToResponse(Inventory inventory) {
		return InventoryResponse.builder()
				.quantity(inventory.getQuantity())
				.lastUpdated(inventory.getLastUpdated().toString())
				.build();
	}
	
	
	//____________________________________________________________________
	
	
	public Pharmacy ConvertPharmacyToEntity(PharmacyRequest request) {
		return Pharmacy.builder()
				.name(request.getName())
				.address(request.getAddress())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.phone(request.getPhone())
				.deliverySupport(request.isDeliverySupport())
				.inventories(new ArrayList<>())
				.orders(new ArrayList<>())
				.owner(urepo.findById(request.getOwnerId()).orElseThrow( () -> new ElementNotFoundException
						("The Owner with This ID ( "+request.getOwnerId()+" ) Not Found") ) )
				.build();
	}
	
	
	public PharmacyResponse ConvertPharmacyToResponse(Pharmacy pharmacy) {
		return PharmacyResponse.builder()
				.id(pharmacy.getId())
				.name(pharmacy.getName())
				.address(pharmacy.getAddress())
				.latitude(pharmacy.getLatitude())
				.longitude(pharmacy.getLongitude())
				.distance(pharmacy.getDistance())
				.phone(pharmacy.getPhone())
				.deliverySupport(pharmacy.isDeliverySupport())
				.build();
	}
	
	
	public InventoryDto ConvertPharmacyToDto(Pharmacy pharmacy) {
		return InventoryDto.builder()
				.pmId(pharmacy.getId())
				.pmName(pharmacy.getName())
				.ImageUrl(pharmacy.getImageUrl())
				.build();
	}
	
	//____________________________________________________________________
	
	
	public User ConvertUserToEntity(UserRequest request) {
		return User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.password(myPasswordEncoder.encode(request.getPassword())) 
				.phone(request.getPhone())
				.orders(new ArrayList<>())
				.role("ROLE_" + request.getRole().toUpperCase())
				.build();
	}
	
	public UserResponse ConvertUserToResponse(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.phone(user.getPhone())
				.build();
	}
	
	
	//____________________________________________________________________
	
	public Order ConvertOrderToEntity(OrderRequest request) {
		return Order.builder()
				.status(OrderStatus.PENDING.toString())
				.createdAt(Timestamp.valueOf(LocalDateTime.now()))
				.user( urepo.findById(request.getUserId())
						.orElseThrow( () -> new ElementNotFoundException("User with this id : " + request.getUserId() + " -> not found") ) )
				//.pharmacy( prepo.findById(request.getPharmacyId())
						//.orElseThrow(  () -> new ElementNotFoundException("Pharmacy with this id : " + request.getPharmacyId() + " -> not found")) )
				.orderItems( request.getOrderItems().stream()
						.map( req ->  ConvertOrderItemToEntity(req) )
						.collect(Collectors.toList()) )
				.build();
	}
					
				
	public OrderResponse ConvertOrderToResponse(Order order) {
		return OrderResponse.builder()
				.id(order.getId())
				.createdAt(order.getCreatedAt().toString())
				.status(order.getStatus().toString())
				.totalPrice(order.getTotalPrice())
				.userId(order.getUser().getId())
				.pharmacyId(order.getPharmacy().getId())
				.orderItems(order.getOrderItems().stream()
						.map( ent -> ConvertOrderItemToResponse(ent))
						.collect(Collectors.toList()) )
				.build();
	}
	
	public OrderDto ConvertOrderToDto(Order order) {
		return OrderDto.builder()
				.id(order.getId())
				.createdAt(order.getCreatedAt().toString())
				.status(order.getStatus().toString())
				.totalPrice(order.getTotalPrice())
				.build();
	}
		
		
	//____________________________________________________________________
		
	public OrderItem ConvertOrderItemToEntity(OrderItemRequest request) {
		return OrderItem.builder()
				.medicine(mrepo.findById(request.getMedicineId())
						.orElseThrow(  () -> new ElementNotFoundException("medicine with this id : " + request.getMedicineId() + " -> not found")) )
				.quantity(request.getQuantity())
				.price( request.getQuantity() * mrepo.getMedicinePrice(request.getMedicineId()))
				.build();
	}
	
	public OrderItemResponse ConvertOrderItemToResponse(OrderItem item) {
		return OrderItemResponse.builder()
				.id(item.getId())
				.medicineId(item.getMedicine().getId())
				.quantity(item.getQuantity())
				.price(item.getPrice())
				.build();
	}
		
		
	//____________________________________________________________________
		
	
	public double CalculateHaversine
				(double userlat , double userlong , double pharmalat , double pharmalong) {
		
		final int R = 6371;
        double diffLat = Math.toRadians(pharmalat - userlat);
        double diffLong = Math.toRadians(pharmalong - userlong);
        double a = Math.sin(diffLat / 2) * Math.sin(diffLat / 2) +
                   Math.cos(Math.toRadians(userlat)) * Math.cos(Math.toRadians(pharmalat)) *
                   Math.sin(diffLong / 2) * Math.sin(diffLong / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
	}
	
	//____________________________________________________________________
	
	public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}
