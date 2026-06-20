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
public class OrderRequest {

	private Long userId;
	private double userLat;
	private double userLong;
	
	private List<OrderItemRequest> orderItems;
}
