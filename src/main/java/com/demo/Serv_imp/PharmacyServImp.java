package com.demo.Serv_imp;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.demo.Entities.Inventory;	
import com.demo.Entities.Pharmacy;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.IO.AppResponse;
import com.demo.IO.InventoryDto;
import com.demo.IO.PharmacyRequest;
import com.demo.Repositories.InventoryRepository;
import com.demo.Repositories.OrderRepository;
import com.demo.Repositories.PharmacyRepository;
import com.demo.Services.PharmacyServices;
import com.demo.Utils.AppUtils;

@Service
public class PharmacyServImp implements PharmacyServices {
	
	@Autowired
	PharmacyRepository prepo;
	
	@Autowired
	InventoryRepository irepo;
	
	@Autowired
	OrderRepository orepo;
	
	@Autowired
	AppUtils utils;
	
	@Autowired
	Cloudinary cloudinary;
	
	double maxlat , minlat , maxlong , minlong;

	@Override
	public AppResponse CreatePharmacy(PharmacyRequest request, MultipartFile image) throws IOException {
		
		Map uploadRequest = cloudinary.uploader().upload(image.getBytes() ,
				ObjectUtils.asMap("upload_preset","pharmaGo") );
		
		Pharmacy pharmacy = utils.ConvertPharmacyToEntity(request);
		
		pharmacy.setImageUrl(uploadRequest.get("secure_url").toString());
		pharmacy.setImagePublicId(uploadRequest.get("public_id").toString() );
		
		pharmacy = prepo.save(pharmacy);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setSuccessMessage("Pharmacy Created Successfully...");
		
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse DeletePharmacy(Long id) {
		
		Pharmacy pharmacy = prepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Pharmacy with Id ( " + id + " ) Not Found"));
		prepo.delete(pharmacy);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("Pharmacy Deleted Successfully...");
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse UpdatePharmacy(Long id, PharmacyRequest request) {
		
		Pharmacy pharmacy = prepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Pharmacy with Id ( " + id + " ) Not Found"));
		
		pharmacy.setName(request.getName());
		pharmacy.setAddress(request.getAddress());
		pharmacy.setLatitude(request.getLatitude());
		pharmacy.setLongitude(request.getLongitude());
		pharmacy.setPhone(request.getPhone());
		pharmacy.setDeliverySupport(request.isDeliverySupport());
		pharmacy = prepo.save(pharmacy);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("Pharmacy Updated Successfully...");
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse GetPharmacyById(Long id) {
		
		Pharmacy pharmacy = prepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Pharmacy with Id ( " + id + " ) Not Found"));
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setPharmacy( utils.ConvertPharmacyToResponse(pharmacy) );
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse GetAllPharmacies() {

		AppResponse response = new AppResponse();
		response.setInventoryDtoList( prepo.findAll().stream()
				.map( ent -> utils.ConvertPharmacyToDto(ent))
				.collect(Collectors.toList()) );
		response.setStatusCode(200);
		
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse FindNearbyPharmacies(double userlat, double userlong , double distance) {
		
		maxlat = userlat + (distance/111.0);
		minlat = userlat - (distance/111.0);
		maxlong = userlong + (distance / (111.0 * Math.cos(userlat)));
		minlong = userlong - (distance / (111.0 * Math.cos(userlat)));
		
		List<Pharmacy> pharmaciesInBox = prepo.FindPharmaciesInWithBoundingBox
												(maxlat, minlat, maxlong, minlong);
		
		if(pharmaciesInBox.isEmpty()) {
			System.out.println("List is empty $$$$$$$");
			System.out.println(maxlat);
			System.out.println(minlat);
			System.out.println(maxlong);
			System.out.println(minlong);
		}
		
		AppResponse response = new AppResponse();
		
		response.setPharmacyList( 
				pharmaciesInBox.stream().peek( p -> {
				double dist = utils.CalculateHaversine(userlat, userlong, p.getLatitude(), p.getLongitude());
				p.setDistance(dist);
			})
			.filter(pharma -> pharma.getDistance() <= distance)
			.sorted(Comparator.comparingDouble(Pharmacy::getDistance))
			.map( pharma -> utils.ConvertPharmacyToResponse(pharma))
			.collect(Collectors.toList()) 
		);
		
		response.setStatusCode(200);	
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse GetPharmacyOrders(Long id) {

		Pharmacy pharmacy = prepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Pharmacy with Id ( " + id + " ) Not Found"));
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setOrderList( pharmacy.getOrders().stream()
					.map( ent -> utils.ConvertOrderToResponse(ent))
					.collect(Collectors.toList()) );
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse GetLowStockAlerts(Long pharmaId) {
		
		AppResponse response = new AppResponse();
		response.setSuccessMessage("No Medicines In Low Stock..");
		
		List<Inventory> lowStockMedicines = irepo.FindLowStockMedicinesInPharmacy(pharmaId);
		if(!lowStockMedicines.isEmpty()) {
			response.setInventoryDtoList( lowStockMedicines.stream()
					.map( ent -> new InventoryDto(ent.getMedicine().getId() ,
												  ent.getMedicine().getName() 
												, ent.getMedicine().getImageUrl() ) )
					.collect(Collectors.toList()) );
			response.setSuccessMessage("Medicines In Low Stock Founded..");
		}	
		response.setStatusCode(200);
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse GetDailySales(Long pharmacyId , LocalDate date) {
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setOrderList( orepo.findOrdersByPharmacyAndDate(pharmacyId, date).stream()
					.map( ent -> utils.ConvertOrderToResponse(ent))
					.collect(Collectors.toList()) );
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse GetPharmacyOwner(Long id) {
		
		Pharmacy pharmacy = prepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Pharmacy with Id ( " + id + " ) Not Found"));
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setUser( utils.ConvertUserToResponse(pharmacy.getOwner()) );
		return response;
	}
	
	//----------------------------------------------------

	@Override
	public AppResponse FindNearbyPharmaciesForSpecificMedicine(double userlat , double userlong , double distance , Long medicineId) {
		
		maxlat = userlat + (distance / 111.0);
		minlat = userlat - (distance/111.0);
		maxlong = userlong + (distance / (111.0 * Math.cos(userlat)));
		minlong = userlong - (distance / (111.0 * Math.cos(userlat)));
		
		List<Pharmacy> foundedPharmacies = prepo.FindNearbyPharmaciesForMedicine
											(medicineId, maxlat, minlat, maxlong, minlong);
		
		AppResponse response = new AppResponse();
		response.setPharmacyList(
			foundedPharmacies.stream().peek( p -> {
				double dist = utils.CalculateHaversine(userlat, userlong, p.getLatitude(), p.getLongitude());
				p.setDistance(dist);
			})
			.filter( p -> p.getDistance() <= distance)
			.sorted(Comparator.comparingDouble(Pharmacy::getDistance))
			.map( pharma -> utils.ConvertPharmacyToResponse(pharma))
			.collect(Collectors.toList()) 
		);
	
		response.setStatusCode(200);	
		return response;
	}

}
