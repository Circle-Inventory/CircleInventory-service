package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.exceptions.CustomException;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public void assignRoleToUser(String email, String role) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }
}
