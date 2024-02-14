package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.model.Category;
import com.toholanka.inventorybackend.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> listCategory() {
        return categoryRepository.findAll();
    }

    public void editCategory(String categoryId, Category updateCategory) {
        Category category = categoryRepository.getById(categoryId);
        category.setCategoryName(updateCategory.getCategoryName());
        category.setDescription(updateCategory.getDescription());
        category.setTotalItem(updateCategory.getTotalItem());
        category.setStatus(updateCategory.getStatus());
        categoryRepository.save(category);
    }

    public boolean findById(String categoryId) {
        return categoryRepository.findById(categoryId).isPresent();
    }

    public void deleteCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Category not found with ID: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }
}
