package com.toholanka.inventorybackend.controller;

import com.toholanka.inventorybackend.common.ApiResponse;
import com.toholanka.inventorybackend.dto.user.RoleAssignDto;
import com.toholanka.inventorybackend.dto.user.UserCreateDto;
import com.toholanka.inventorybackend.dto.user.UserInviteDto;
import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.service.AdminService;
import com.toholanka.inventorybackend.service.AuthenticationService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final AuthenticationService authenticationService;

    public AdminController(AdminService adminService, AuthenticationService authenticationService) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/list")
    public List<Users> getAllUsers() { return adminService.getAllUsers(); }

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

    @PostMapping("/createUser")
    public ResponseEntity<ApiResponse> partnerAdd(@RequestHeader("Authorization") String authHeader, @RequestBody UserCreateDto userCreateDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);

        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            adminService.createUser(userCreateDto);
            return ResponseEntity.ok(new ApiResponse(true, "Partner add successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to send invite mail."));
        }
    }

    @PostMapping("/inviteUser")
    public ResponseEntity<ApiResponse> mailSender(@RequestHeader("Authorization") String authHeader, @RequestBody UserInviteDto userInviteDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);

        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            adminService.inviteMailSend(userInviteDto);
            return ResponseEntity.ok(new ApiResponse(true, "Partner add successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to send invite mail."));
        }
    }



    @PutMapping("/editUser/{email}")
    public ResponseEntity<ApiResponse> editUserDetails(@RequestHeader("Authorization") String authHeader, @PathVariable String email, @RequestBody UserCreateDto userCreateDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);

        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            adminService.editUserDetails(email, userCreateDto);
            return ResponseEntity.ok(new ApiResponse(true, "User Details edit successfully."));
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
