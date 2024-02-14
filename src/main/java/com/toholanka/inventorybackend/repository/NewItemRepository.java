package com.toholanka.inventorybackend.repository;

import com.toholanka.inventorybackend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewItemRepository extends JpaRepository<Item, String> {
    Item getById(String itemId);

    void deleteById(String itemId);

    Optional<Item> findById(String itemId);
}
