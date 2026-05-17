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
	//private Long pharmacyId;
	private List<OrderItemRequest> orderItems;
}
