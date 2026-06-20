package com.demo.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.demo.IO.InventoryRequest;
import com.demo.Serv_imp.InventoryServImp;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryServImp inventoryService;

    // ================= ADD MEDICINE =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PostMapping
    public ResponseEntity<AppResponse> addMedicine(
            @RequestBody InventoryRequest request
    ) {

        AppResponse response = inventoryService.AddMedicineToPharmacy(request);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= UPDATE INVENTORY =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PutMapping
    public ResponseEntity<AppResponse> updateInventory(
            @RequestBody InventoryRequest request
    ) {

        AppResponse response = inventoryService.UpdateInventory(request);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= DELETE INVENTORY =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @DeleteMapping
    public ResponseEntity<AppResponse> deleteInventory(
            @RequestParam Long pharmacyId,
            @RequestParam Long medicineId
    ) {

        AppResponse response = inventoryService.DeleteInventory(pharmacyId, medicineId);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= GET INVENTORY =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping
    public ResponseEntity<AppResponse> getInventory(
            @RequestParam Long pharmacyId,
            @RequestParam Long medicineId
    ) {

        AppResponse response = inventoryService.GetInventoryByIds(pharmacyId, medicineId);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= CHECK AVAILABILITY =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY', 'USER')")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long pharmacyId,
            @RequestParam Long medicineId,
            @RequestParam int quantity
    ) {

        boolean available = inventoryService.CheckMedicineAvailability(
                pharmacyId,
                medicineId,
                quantity
        );

        return ResponseEntity.ok(available);
    }
}
