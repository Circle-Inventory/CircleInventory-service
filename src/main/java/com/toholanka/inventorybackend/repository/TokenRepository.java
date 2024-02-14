package com.toholanka.inventorybackend.repository;

import com.toholanka.inventorybackend.model.AuthenticationToken;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.util.ItemIdGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, ItemIdGenerator> {

    AuthenticationToken findByUser(Users user);

    AuthenticationToken findByToken(String token);
}
