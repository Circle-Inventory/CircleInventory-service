package com.toholanka.inventorybackend.controller;

import com.toholanka.inventorybackend.common.ApiResponse;
import com.toholanka.inventorybackend.dto.user.RoleAssignDto;
import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.service.AdminService;
import com.toholanka.inventorybackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/assignRole")
    public ResponseEntity<ApiResponse> assignRoleToUser(@RequestHeader("Authorization") String authHeader, @RequestBody RoleAssignDto request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);

        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            adminService.assignRoleToUser(request.getEmail(), request.getRole());
            return ResponseEntity.ok(new ApiResponse(true, "Role assigned successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ApiResponse> deleteUser(@RequestHeader("Authorization") String authHeader, @RequestParam("email") String email) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);

        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            adminService.deleteUser(email);
            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
