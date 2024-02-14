package com.toholanka.inventorybackend.repository;

import com.toholanka.inventorybackend.model.Category;
import com.toholanka.inventorybackend.util.CategoryIdGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByCategoryName(String categoryName);

    Category getById(String categoryId);

    Optional<Category> findById(String categoryId);

    boolean existsById(String categoryId);

    void deleteById(String categoryId);
}
