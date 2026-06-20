package com.demo.Services;

import org.springframework.stereotype.Service;

import com.demo.IO.AppResponse;
import com.demo.IO.OrderRequest;


public interface OrderServices {
	
	public AppResponse CreateOrder(OrderRequest request);
	
	public AppResponse CancelOrder(Long orderId, Long userId);
	
	public AppResponse confirmOrder(Long orderId);
	
	public AppResponse GetOrderById(Long orderId);
	
	public AppResponse GetAllOrders();
	
	public AppResponse rejectOrder(Long orderId);
	

}
