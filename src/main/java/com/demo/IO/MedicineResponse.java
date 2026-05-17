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
public class MedicineResponse {

	private Long id;
	private String name;
	private String description;
	private String category;
	private double price;
	private String imageUrl;
	private boolean requiresPrescription;
	
}

/*
 * private Long id; private String name; private String category; private String
 * imageUrl; private String imagePublicId; private String description; private
 * boolean requiresPrescription; private List<InventoryResponse> inventories;
 */
