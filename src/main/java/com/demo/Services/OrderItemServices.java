package com.demo.Services;

import com.demo.IO.OrderItemRequest;

public interface OrderItemServices {

	public void CreateOrderItem(OrderItemRequest request);
	
	public void DeleteOrderItem(Long id);
}
