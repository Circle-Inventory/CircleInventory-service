package com.toholanka.inventorybackend.controller;

import com.toholanka.inventorybackend.common.ApiResponse;
import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.model.Category;
import com.toholanka.inventorybackend.service.AuthenticationService;
import com.toholanka.inventorybackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestHeader("Authorization") String authHeader, @RequestBody Category category) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);
        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            categoryService.createCategory(category);
            return new ResponseEntity<>(new ApiResponse(true, "a new category created"), HttpStatus.CREATED);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/list")
    public List<Category> listCategory() {
        return categoryService.listCategory();
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestHeader("Authorization") String authHeader, @PathVariable("categoryId") String categoryId, @RequestBody Category category ) {
        if (!categoryService.findById(categoryId)) {
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category does not exists"), HttpStatus.NOT_FOUND);
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);
        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            categoryService.editCategory(categoryId, category);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "category has been updated"), HttpStatus.OK);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestHeader("Authorization") String authHeader, @PathVariable("categoryId") String categoryId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);
        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }
            
            categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(new ApiResponse(true, "category has been deleted"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, "An error occurred while deleting the category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
