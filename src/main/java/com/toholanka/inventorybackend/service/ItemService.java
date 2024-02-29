package com.toholanka.inventorybackend.service;

import com.toholanka.inventorybackend.model.Category;
import com.toholanka.inventorybackend.model.Item;
import com.toholanka.inventorybackend.repository.CategoryRepository;
import com.toholanka.inventorybackend.repository.NewItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final NewItemRepository newItemRepository;

    private final CategoryRepository categoryRepository;

    public ItemService(NewItemRepository newItemRepository, CategoryRepository categoryRepository) {
        this.newItemRepository = newItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Item addItem(Item item) {

        String categoryName = item.getCategory();
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Long totalItem = category.getTotalItem();
        category.setTotalItem(totalItem + item.getQuantity());

        return newItemRepository.save(item);
    }

    public Item updateItem(String itemId, Item updateItem) {
        Item item = newItemRepository.getById(itemId);
        item.setItemName(updateItem.getItemName());
        item.setModelNumber(updateItem.getModelNumber());
        item.setCategory(updateItem.getCategory());
        item.setBrandName(updateItem.getBrandName());
        item.setColor(updateItem.getColor());
        item.setGrossPrice(updateItem.getGrossPrice());
        item.setQuantity(updateItem.getQuantity());
        item.setImageUrl(updateItem.getImageUrl());
        item.setTags(updateItem.getTags());
        item.setNetPrice(updateItem.getNetPrice());
        return newItemRepository.save(item);
    }

    public void deleteItem(String itemId) {
        Optional<Item> optionalItem = newItemRepository.findById(itemId);
        Item item = optionalItem.get();
        String categoryName = item.getCategory();
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Long totalItem = category.getTotalItem() - item.getQuantity();
        category.setTotalItem(totalItem);
        newItemRepository.deleteById(itemId);
    }

    public Optional<Item> getItem(String itemId) {
        return newItemRepository.findById(itemId);
    }

    public boolean findById(String itemId) {
        return newItemRepository.findById(itemId).isPresent();
    }

    public List<Item> getAllItems() {
        return newItemRepository.findAll();
    }
}
