package com.demo.Serv_imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Entities.OrderItem;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.IO.OrderItemRequest;
import com.demo.Repositories.OrderItemRepository;
import com.demo.Services.OrderItemServices;
import com.demo.Utils.AppUtils;

@Service
public class OrderItemServImp implements OrderItemServices{
	
	@Autowired
	AppUtils utils;
	
	@Autowired
	OrderItemRepository oirepo;

	@Override
	public void CreateOrderItem(OrderItemRequest request) {
		
		OrderItem oitem = utils.ConvertOrderItemToEntity(request);
		oirepo.save(oitem);
	}

	@Override
	public void DeleteOrderItem(Long id) {
		OrderItem oitem = oirepo.findById(id)
				.orElseThrow(  () -> new ElementNotFoundException("OrderItem with this id : " + id + " -> not found"));
		oirepo.delete(oitem);	
	}

}
