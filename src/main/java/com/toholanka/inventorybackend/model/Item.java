package com.toholanka.inventorybackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(generator = "item_id_generator")
    @GenericGenerator(
            name = "item_id_generator",
            strategy = "com.toholanka.inventorybackend.util.ItemIdGenerator"
    )
    @Column(name = "itemId", nullable = false, unique = true)
    private String itemId;

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "modelNumber")
    private String modelNumber;

    @Column(name = "brandName")
    private String brandName;

    @Column(name = "category")
    private String category;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "year")
    private Integer year;

    @Column(name = "grossPrice")
    private Double grossPrice;

    @Column(name = "color")
    private String color;

    @Column(name = "tags")
    private String tags;

    @Column(name = "netPrice")
    private Double netPrice;

    @Column(name = "imageUrl")
    private String imageUrl;

    // getters, and setters


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() { return itemName; }

    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getModelNumber() { return modelNumber; }

    public void setModelNumber(String modelNumber) { this.modelNumber = modelNumber; }

    public String getBrandName() { return brandName; }

    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getGrossPrice() { return grossPrice; }

    public void setGrossPrice(Double grossPrice) { this.grossPrice = grossPrice; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getTags() { return tags; }

    public void setTags(String tags) { this.tags = tags; }

    public Double getNetPrice() { return netPrice; }

    public void setNetPrice(Double netPrice) { this.netPrice = netPrice; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
