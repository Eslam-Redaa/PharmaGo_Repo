package com.demo.Controllers;


import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.demo.IO.AppResponse;
import com.demo.IO.MedicineRequest;
import com.demo.Serv_imp.MedicineServImp;



@RestController
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private MedicineServImp medicineService;

    // ================= ADD MEDICINE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PostMapping("/add")
    public ResponseEntity<AppResponse> addMedicine(
    			@ModelAttribute ("request") MedicineRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        AppResponse response = medicineService.addMedicine(request, image);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ================= UPDATE MEDICINE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateMedicine(
            @PathVariable Long id,
            @RequestBody MedicineRequest request
    ) {
        AppResponse response = medicineService.updateMedicine(id, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ================= DELETE MEDICINE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse> deleteMedicine(@PathVariable Long id) {
        AppResponse response = medicineService.deleteMedicine(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // ================= GET ALL =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping
    public ResponseEntity<AppResponse> getAllMedicines() {
        AppResponse response = medicineService.getAllMedicines();
        return ResponseEntity.ok(response);
    }

    // ================= GET BY ID =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getMedicineById(@PathVariable Long id) {
        AppResponse response = medicineService.getMedicineById(id);
        return ResponseEntity.ok(response);
    }

    // ================= SEARCH BY NAME =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/search")
    public ResponseEntity<AppResponse> searchByName(@RequestParam String name) {
        AppResponse response = medicineService.getMedicinesByName(name);
        return ResponseEntity.ok(response);
    }

    // ================= SEARCH BY CATEGORY =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/category")
    public ResponseEntity<AppResponse> searchByCategory(@RequestParam String category) {
        AppResponse response = medicineService.searchByCategory(category);
        return ResponseEntity.ok(response);
    }

    // ================= MEDICINES IN PHARMACY =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<AppResponse> getMedicinesInPharmacy(@PathVariable Long pharmacyId) {
        AppResponse response = medicineService.GetMedicinesInPharmacy(pharmacyId);
        return ResponseEntity.ok(response);
    }

    // ================= PRESCRIPTION REQUIRED =================
    @GetMapping("/prescription")
    public ResponseEntity<AppResponse> getRequiresPrescription() {
        AppResponse response = medicineService.getRequiresPrescription();
        return ResponseEntity.ok(response);
    }

    // ================= OTC =================
    @GetMapping("/otc")
    public ResponseEntity<AppResponse> getNotRequiresPrescription() {
        AppResponse response = medicineService.getNotRequiresPrescription();
        return ResponseEntity.ok(response);
    }

    // ================= CHANGE IMAGE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PutMapping("/{id}/image")
    public ResponseEntity<AppResponse> changeImage(
            @PathVariable Long id,
            @RequestParam MultipartFile image
    ) {
        AppResponse response = medicineService.changeMedicineImage(id, image);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    // ================= GET CATEGORIES =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(medicineService.getCategoriesList());
    }

    // ================= AUTOCOMPLETE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam String prefix) {
        return ResponseEntity.ok(medicineService.findTop7ByNameStartingWith(prefix));
    }
}
