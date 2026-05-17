package com.demo.Serv_imp;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.demo.Entities.Inventory;
import com.demo.Entities.Medicine;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.IO.AppResponse;
import com.demo.IO.MedicineRequest;
import com.demo.Repositories.InventoryRepository;
import com.demo.Repositories.MedicineRepository;
import com.demo.Services.MedicineServices;
import com.demo.Utils.AppUtils;

@Service
public class MedicineServImp implements MedicineServices{
	
	@Autowired
	AppUtils utils;
	
	@Autowired
	MedicineRepository mrepo;
	
	@Autowired
	InventoryRepository irepo;
	
	@Autowired
	Cloudinary cloudinary;
	

	@Override
	public AppResponse addMedicine(MedicineRequest request , MultipartFile image) throws IOException {
		
		Map uploadRequest = cloudinary.uploader().upload(image.getBytes() ,
				ObjectUtils.asMap("upload_preset","pharmaGo") );
		
		Medicine medicine = utils.ConvertMedicineToEntity(request);
		medicine.setImageUrl(uploadRequest.get("secure_url").toString());
		medicine.setImagePublicId(uploadRequest.get("public_id").toString() );
		medicine = mrepo.save(medicine);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setSuccessMessage("Medicine Created Successfully...");
		
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse deleteMedicine(Long id) {
		
		Medicine medicine = mrepo.findById(id)
							.orElseThrow( () -> new ElementNotFoundException("Medicine with Id ( " + id + " ) Not Found"));
		// delete photo from cloud
		mrepo.delete(medicine);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("Medicine Deleted Successfully...");
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse updateMedicine(Long id, MedicineRequest request) {
		Medicine medicine = mrepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Medicine with Id ( " + id + " ) Not Found"));
		medicine.setName(request.getName());
		medicine.setCategory(request.getCategory());
		medicine.setDescription(request.getDescription());
		medicine.setRequiresPrescription(request.isRequiresPrescription());
		medicine = mrepo.save(medicine);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("Medicine Updated Successfully...");
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse getMedicinesByName(String name) {
		
		List<Medicine> medicines = mrepo.findByName(name);
		if(medicines.isEmpty()) {
			throw new ElementNotFoundException("There is not Medicine With this name : "+name);
		}
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setInventoryDtoList( medicines.stream()
				.map( ent -> utils.ConvertMedicineToDto(ent))
				.collect(Collectors.toList()));
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse getMedicineById(Long id) {
		
		Medicine medicine = mrepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Medicine with Id ( " + id + " ) Not Found"));
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setMedicine(utils.ConvertMedicineToResponse(medicine));
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse getAllMedicines() {
		
		AppResponse response = new AppResponse();
		response.setInventoryDtoList( mrepo.findAll().stream()
				.map( ent -> utils.ConvertMedicineToDto(ent))
				.collect(Collectors.toList()));
		response.setStatusCode(200);
		
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse searchByCategory(String category) {

		AppResponse response = new AppResponse();
		response.setInventoryDtoList( mrepo.findByCategory(category).stream()
				.map( ent -> utils.ConvertMedicineToDto(ent) )
				.collect(Collectors.toList()));
		response.setStatusCode(200);
		
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse getRequiresPrescription() {
		
		AppResponse response = new AppResponse();
		response.setInventoryDtoList( mrepo.findByrequiresPrescriptionTrue().stream()
				.map( ent -> utils.ConvertMedicineToDto(ent))
				.collect(Collectors.toList()));
		response.setStatusCode(200);
		
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse getNotRequiresPrescription() {
		
		AppResponse response = new AppResponse();
		response.setInventoryDtoList( mrepo.findByrequiresPrescriptionFalse().stream()
				.map( ent -> utils.ConvertMedicineToDto(ent))
				.collect(Collectors.toList()));
		response.setStatusCode(200);
		
		return response;
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse changeMedicineImage(Long id, MultipartFile image) {
		
		 // check medicine exists
	    Medicine medicine = mrepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Medicine not found"));

	    try {

	        // delete old image from cloudinary if exists
	        if (medicine.getImagePublicId() != null &&
	            !medicine.getImagePublicId().isEmpty()) {

	            cloudinary.uploader().destroy(
	                    medicine.getImagePublicId(),
	                    ObjectUtils.emptyMap()
	            );
	        }

	        // upload new image
	        Map uploadResult = cloudinary.uploader().upload(
	                image.getBytes(),
	                ObjectUtils.emptyMap()
	        );

	        // save new image data
	        medicine.setImageUrl(uploadResult.get("secure_url").toString());
	        medicine.setImagePublicId(uploadResult.get("public_id").toString());

	        mrepo.save(medicine);

	        AppResponse response = new AppResponse();
			response.setStatusCode(200);
			response.setSuccessMessage("Photo Changed Successfully...");
			return response;

	    } catch (IOException e) {

	    	AppResponse response = new AppResponse();
			response.setStatusCode(200);
			response.setErrorMessage("Faild to upload image");
			return response;
	    }
	}
	
	//------------------------------------------------------

	@Override
	public List<String> getCategoriesList() {

		return mrepo.findCategoriesList();
	}
	
	//------------------------------------------------------

	@Override
	public List<String> findTop7ByNameStartingWith(String prefix) {

		return mrepo.findByNameStartingWith(prefix, PageRequest.of(0, 7)).stream()
				.map(Medicine::getName)
				.collect(Collectors.toList());
	}
	
	//------------------------------------------------------

	@Override
	public AppResponse GetMedicinesInPharmacy(Long pharmaId) {

		AppResponse response = new AppResponse();
		response.setInventoryDtoList(irepo.GetMedicinesInPharma(pharmaId).stream()
				.map(Inventory::getMedicine)
				.map( ent -> utils.ConvertMedicineToDto(ent))
				.collect(Collectors.toList()));
		response.setStatusCode(200);
		
		return response;
		
	}
	
	//------------------------------------------------------

	@Override
	public double GetMedicinePriceById(Long id) {

		Medicine medicine = mrepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("Medicine with Id ( " + id + " ) Not Found"));
		
		return medicine.getPrice();
	}
	

}
