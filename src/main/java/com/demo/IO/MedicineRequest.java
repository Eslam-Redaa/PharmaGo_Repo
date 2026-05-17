package com.demo.IO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequest {

	private String name;
	private String category;
	private String description;
	private double price;
	private boolean requiresPrescription;
	
}
