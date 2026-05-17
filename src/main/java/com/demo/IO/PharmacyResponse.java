package com.demo.IO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyResponse {

	private Long id;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private double distance;
	private String phone;
	private boolean deliverySupport;
}
