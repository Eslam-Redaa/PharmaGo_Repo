package com.demo.IO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

	private Long medicineId;
	private Long pharmacyId;
	private int quantity;
	//private double price;
}
