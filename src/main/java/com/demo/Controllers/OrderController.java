package com.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.demo.IO.AppResponse;
import com.demo.IO.OrderRequest;
import com.demo.Serv_imp.OrderServImp;
import com.demo.Services.OrderServices;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServImp orderService;

    // ================= CREATE ORDER =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @PostMapping
    public ResponseEntity<AppResponse> createOrder(
            @RequestBody OrderRequest request
    ) {

        AppResponse response = orderService.CreateOrder(request);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= CANCEL ORDER =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @PutMapping("/cancel")
    public ResponseEntity<AppResponse> cancelOrder(
    			@RequestParam Long orderId,
            @RequestParam Long userId
    ) {

        AppResponse response = orderService.CancelOrder(orderId, userId);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }
    
    // ================= CONFIRME ORDER =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PutMapping("/confirm")
    public ResponseEntity<AppResponse> confirmOrder( @RequestParam Long orderId){

        return ResponseEntity.ok(
                orderService.confirmOrder(orderId)
        );
    }
    
    	// ================= REJECT ORDER =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PutMapping("/reject")
    public ResponseEntity<AppResponse> rejectOrder( @RequestParam Long orderId ){

        return ResponseEntity.ok(
                orderService.rejectOrder(orderId)
        );
    }
    
    
	// ================= FIND ORDER BY ID =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<AppResponse> getOrderById(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
        		orderService.GetOrderById(orderId)
        );
    }
    
    
	// ================= FIND ALL ORDERS =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/all")
    public ResponseEntity<AppResponse> getAllOrders() {

        return ResponseEntity.ok(
        		orderService.GetAllOrders()
        );
    }
}