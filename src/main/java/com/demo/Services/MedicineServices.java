package com.demo.Services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.demo.IO.AppResponse;
import com.demo.IO.MedicineRequest;

public interface MedicineServices {
	
	public AppResponse addMedicine(MedicineRequest request , MultipartFile image) throws IOException; 
	
	public AppResponse deleteMedicine(Long id);
	
	public AppResponse updateMedicine(Long id , MedicineRequest request);

	public AppResponse getMedicinesByName(String name);
	
	public AppResponse getMedicineById(Long id);
	
	public AppResponse getAllMedicines();
	
	public AppResponse searchByCategory(String category);
	
	public AppResponse GetMedicinesInPharmacy(Long pharmacyId); 
	
	public AppResponse getRequiresPrescription();
	
	public AppResponse getNotRequiresPrescription();
	
	public AppResponse changeMedicineImage(Long id , MultipartFile image);  
		
	
	//________________________________________________
	
	public double GetMedicinePriceById(Long id);
	
	public List<String> getCategoriesList();
	
	public List<String> findTop7ByNameStartingWith(String prefix); 
	
}

