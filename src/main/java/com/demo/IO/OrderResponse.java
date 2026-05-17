package com.demo.IO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private Long id;
	private String status;
	private double totalPrice;
	private String createdAt;
	
	private Long userId;
	private Long pharmacyId;
	private List<OrderItemResponse> orderItems;
}
