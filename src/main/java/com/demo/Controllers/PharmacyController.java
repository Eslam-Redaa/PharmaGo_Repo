package com.demo.Controllers;

import java.io.IOException;
import java.time.LocalDate;

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
import com.demo.IO.PharmacyRequest;
import com.demo.Serv_imp.PharmacyServImp;
import com.demo.Services.PharmacyServices;

@RestController
@RequestMapping("/pharmacies")
public class PharmacyController {

    @Autowired
    private PharmacyServImp pharmacyService;

    // ================= CREATE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AppResponse> createPharmacy(
            @ModelAttribute ("request") PharmacyRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {

        AppResponse response = pharmacyService.CreatePharmacy(request, image);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= GET ALL =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping
    public ResponseEntity<AppResponse> getAllPharmacies() {

        AppResponse response = pharmacyService.GetAllPharmacies();

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= GET BY ID =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getPharmacyById(@PathVariable Long id) {

        AppResponse response = pharmacyService.GetPharmacyById(id);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= UPDATE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updatePharmacy(
            @PathVariable Long id,
            @RequestBody PharmacyRequest request
    ) {

        AppResponse response = pharmacyService.UpdatePharmacy(id, request);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= DELETE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse> deletePharmacy(@PathVariable Long id) {

        AppResponse response = pharmacyService.DeletePharmacy(id);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= OWNER =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/{id}/owner")
    public ResponseEntity<AppResponse> getPharmacyOwner(@PathVariable Long id) {

        AppResponse response = pharmacyService.GetPharmacyOwner(id);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= ORDERS =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/{id}/orders")
    public ResponseEntity<AppResponse> getPharmacyOrders(@PathVariable Long id) {

        AppResponse response = pharmacyService.GetPharmacyOrders(id);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= NEARBY =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/nearby")
    public ResponseEntity<AppResponse> findNearbyPharmacies(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double distance
    ) {

        AppResponse response = pharmacyService.FindNearbyPharmacies(lat, lng, distance);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= NEARBY MEDICINE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/nearby/medicine")
    public ResponseEntity<AppResponse> findNearbyPharmaciesForMedicine(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double distance,
            @RequestParam Long medicineId
    ) {

        AppResponse response = pharmacyService
                .FindNearbyPharmaciesForSpecificMedicine(lat, lng, distance, medicineId);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= LOW STOCK =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/{id}/low-stock")
    public ResponseEntity<AppResponse> getLowStockAlerts(@PathVariable Long id) {

        AppResponse response = pharmacyService.GetLowStockAlerts(id);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= DAILY SALES =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/{id}/sales")
    public ResponseEntity<AppResponse> getDailySales(
            @PathVariable Long id,
            @RequestParam String date
    ) {

        LocalDate parsedDate = LocalDate.parse(date);

        AppResponse response = pharmacyService.GetDailySales(id, parsedDate);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }
}
