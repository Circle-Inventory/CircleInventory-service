package com.toholanka.inventorybackend.repository;

import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.util.UserIdGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, UserIdGenerator> {
    Optional<Users> findByEmail(String email);

    void deleteByEmail(String email);
}
