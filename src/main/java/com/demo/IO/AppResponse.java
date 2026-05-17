package com.demo.IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.demo.Entities.Inventory;
import com.demo.Entities.Medicine;
import com.demo.Entities.Order;
import com.demo.Entities.OrderItem;
import com.demo.Entities.Pharmacy;
import com.demo.Entities.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppResponse {

	private int statusCode;
	
	private String successMessage;
	
	private String error;
	private String errorMessage;
	private Map<String , String> errorList = new HashMap<>();
	private String errorPath;
	
	private String role;
	private String token;
	private String expirationDate;
	
	private MedicineResponse medicine;
	private InventoryResponse inventory;
	private OrderResponse order;
	private PharmacyResponse pharmacy;
	private UserResponse user;
	private OrderItemResponse orderItem;
	
	private List<MedicineResponse> medicineList = new ArrayList<>();
	private List<InventoryResponse> inventoryList = new ArrayList<>();
	private List<InventoryDto> inventoryDtoList = new ArrayList<>();
	private List<OrderResponse> orderList = new ArrayList<>();
	private List<PharmacyResponse> pharmacyList = new ArrayList<>();
	private List<UserResponse> userList = new ArrayList<>();
	private List<OrderItemResponse> orderItemList = new ArrayList<>();
	
}
