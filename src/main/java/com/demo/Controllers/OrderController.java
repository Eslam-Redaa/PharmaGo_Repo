package com.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<AppResponse> createOrder(
            @RequestBody OrderRequest request,
            @RequestParam double lat,
            @RequestParam double lng
    ) {

        AppResponse response = orderService.CreateOrder(request, lat, lng);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= CANCEL ORDER =================
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<AppResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId
    ) {

        AppResponse response = orderService.CancelOrder(orderId, userId);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }
    
    // ================= CONFIRME ORDER =================
    @PutMapping("/confirm/{orderId}")
    public ResponseEntity<AppResponse> confirmOrder(  @PathVariable Long orderId){

        return ResponseEntity.ok(
                orderService.confirmOrder(orderId)
        );
    }
    
    	// ================= REJECT ORDER =================
    @PutMapping("/reject/{orderId}")
    public ResponseEntity<AppResponse> rejectOrder( @PathVariable Long orderId ){

        return ResponseEntity.ok(
                orderService.rejectOrder(orderId)
        );
    }
    
    
}