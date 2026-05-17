package com.demo.Controllers;

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
import org.springframework.web.bind.annotation.RestController;
import com.demo.IO.AppResponse;
import com.demo.IO.LoginRequest;
import com.demo.IO.UserRequest;
import com.demo.Serv_imp.UserServImp;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServImp userService;

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<AppResponse> login(@RequestBody LoginRequest request) {

        AppResponse response = userService.LogIn(request);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= ADD USER =================
    @PostMapping("/register")
    public ResponseEntity<AppResponse> addUser(@RequestBody UserRequest request) {

        AppResponse response = userService.AddUser(request);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= UPDATE USER =================
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<AppResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        AppResponse response = userService.UpdateUser(id, request);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    // ================= CHANGE EMAIL =================
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/email")
    public ResponseEntity<AppResponse> changeEmail(
            @PathVariable Long id,
            @RequestParam String newEmail) {

        AppResponse response = userService.ChangeEmail(id, newEmail);

        return ResponseEntity.ok(response);
    }

    // ================= DELETE USER =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse> deleteUser(@PathVariable Long id) {

        AppResponse response = userService.DeleteUser(id);

        return ResponseEntity.ok(response);
    }

    // ================= GET BY ID =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.GetUserById(id));
    }

    // ================= GET BY EMAIL =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping("/email")
    public ResponseEntity<AppResponse> getByEmail(@RequestParam String email) {

        return ResponseEntity.ok(userService.GetUserByEmail(email));
    }

    // ================= GET ALL =================
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @GetMapping
    public ResponseEntity<AppResponse> getAll() {

        return ResponseEntity.ok(userService.GetAllUsers());
    }
}
