package com.toholanka.inventorybackend.controller;

import com.toholanka.inventorybackend.common.ApiResponse;
import com.toholanka.inventorybackend.exceptions.AuthenticationFailException;
import com.toholanka.inventorybackend.model.Item;
import com.toholanka.inventorybackend.service.AuthenticationService;
import com.toholanka.inventorybackend.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private final AuthenticationService authenticationService;

    public ItemController(ItemService itemService, AuthenticationService authenticationService) {
        this.itemService = itemService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> addNewItem(@RequestHeader("Authorization") String authHeader, @RequestBody Item item) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);
        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            itemService.addItem(item);
            return new ResponseEntity<>(new ApiResponse(true, "A new item created"), HttpStatus.CREATED);

        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<ApiResponse> updateItem(@RequestHeader("Authorization") String authHeader,
                                                  @PathVariable String itemId,
                                                  @RequestBody Item updateItem) {

        if(!itemService.findById(itemId))
            return new ResponseEntity<>(new ApiResponse(false, "Item does not exists"), HttpStatus.NOT_FOUND);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);
        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }

            itemService.updateItem(itemId, updateItem);
            return new ResponseEntity<>(new ApiResponse(true, "Item has been updated"), HttpStatus.OK);

        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<ApiResponse> deleteItem(@RequestHeader("Authorization") String authHeader, @PathVariable String itemId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid Authorization header format."));
        }
        String token = authHeader.substring(7);
        try {
            boolean isAdmin = authenticationService.validateTokenAndCheckAdminRole(token);
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Access denied."));
            }
            itemService.deleteItem(itemId);
            return new ResponseEntity<>(new ApiResponse(true, "Item has been deleted"), HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, "An error occurred while deleting the item"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable String itemId) {
        return itemService.getItem(itemId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }
}
