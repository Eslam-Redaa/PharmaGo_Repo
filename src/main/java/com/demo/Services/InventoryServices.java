package com.demo.Services;

import com.demo.IO.AppResponse;
import com.demo.IO.InventoryRequest;

public interface InventoryServices {
	
	public AppResponse AddMedicineToPharmacy(InventoryRequest request);
	
	public AppResponse UpdateInventory(InventoryRequest request);
	
	public AppResponse DeleteInventory(Long pharamId , Long medicineId);
	
	public AppResponse GetInventoryByIds(Long pharamId , Long medicineId);
	
	public boolean CheckMedicineAvailability(Long pharmaId, Long medicineId , int quantity);
	
}
