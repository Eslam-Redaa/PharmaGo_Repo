package com.demo.Services;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.demo.IO.AppResponse;
import com.demo.IO.PharmacyRequest;

public interface PharmacyServices {

	public AppResponse CreatePharmacy(PharmacyRequest request , MultipartFile image) throws IOException;
	
	public AppResponse DeletePharmacy(Long id);
	
	public AppResponse UpdatePharmacy(Long id , PharmacyRequest newRequest);
	
	public AppResponse GetPharmacyById(Long id);
	
	public AppResponse GetAllPharmacies();
	
	public AppResponse GetPharmacyOwner(Long id);

	public AppResponse GetPharmacyOrders(Long id);
	
	//-----------------------------------------------------	
	
	public AppResponse FindNearbyPharmacies(double latitude , double longitude , double distance); 
	
	public AppResponse GetLowStockAlerts(Long pharmacyId);  //Cash
	
	public AppResponse GetDailySales(Long pharmacyId , LocalDate date);  // Cash
	
	public AppResponse FindNearbyPharmaciesForSpecificMedicine(double latitude , double longitude , double distance , Long medicineId);
	
}
