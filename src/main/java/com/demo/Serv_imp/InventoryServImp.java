package com.demo.Serv_imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.demo.Entities.Inventory;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.IO.AppResponse;
import com.demo.IO.InventoryRequest;
import com.demo.IO.InventoryResponse;
import com.demo.Repositories.InventoryRepository;
import com.demo.Services.InventoryServices;
import com.demo.Utils.AppUtils;

@Service
public class InventoryServImp implements InventoryServices{
	
	@Autowired
	AppUtils utils;
	
	@Autowired
	InventoryRepository irepo;

	@Override
	public AppResponse AddMedicineToPharmacy(InventoryRequest request) {

		Inventory inventory = utils.ConvertInventoryToEntity(request);
		inventory = irepo.save(inventory);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setSuccessMessage("Inventory Added Successfully...");
		
		return response;
	}

	@Override
	public AppResponse UpdateInventory(InventoryRequest request) {

		Inventory inventory = irepo.findInventoryByIDs(request.getPharmacyId() , request.getMedicineId())
				.orElseThrow( () -> new ElementNotFoundException("Inventory with this Ids Not Found"));
		inventory.setQuantity(request.getQuantity());
		
		inventory = irepo.save(inventory);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("Inventory Updated Successfully...");
		return response;
	}

	@Override
	public AppResponse DeleteInventory(Long pharamId, Long medicineId) {
		Inventory inventory = irepo.findInventoryByIDs(pharamId , medicineId)
				.orElseThrow( () -> new ElementNotFoundException("Inventory with this Ids Not Found"));
		
		irepo.delete(inventory);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("Inventory Deleted Successfully...");
		return response;
	}

	@Override
	public AppResponse GetInventoryByIds(Long pharamId, Long medicineId) {

		Inventory inventory = irepo.findInventoryByIDs(pharamId , medicineId)
				.orElseThrow( () -> new ElementNotFoundException("Inventory with this Ids Not Found"));
		
		InventoryResponse invResponse = new InventoryResponse();
		invResponse = utils.ConvertInventoryToResponse(inventory);
		invResponse.setPharmacyId(pharamId); invResponse.setMedicineId(medicineId);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setInventory( invResponse );
		return response;
	}
	
	//------------------------------------------

	@Override
	public boolean CheckMedicineAvailability(Long pharmaId, Long medicineId, int quantity) {
		
		return irepo.IsMedicineAvailabel(pharmaId, medicineId, quantity);
	}

}
