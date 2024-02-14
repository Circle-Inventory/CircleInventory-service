package com.toholanka.inventorybackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(generator = "category_id_generator")
    @GenericGenerator(
            name = "category_id_generator",
            strategy = "com.toholanka.inventorybackend.util.CategoryIdGenerator"
    )
    @Column(name = "category_id", nullable = false, unique = true)
    private String categoryId;

    @Column(name = "categoryName")
    private @NotBlank String categoryName;

    @Column(name = "description")
    private @NotBlank String description;

    @Column(name = "totalItems")
    private Long totalItem = 0L;

    @Column(name = "dateAdded")
    private Date addDate;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "tag")
    private String tag;

    @Column(name = "status")
    private Boolean status;


    //getters and setters

    public String getCategoryId() { return categoryId; }

    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTotalItem() { return totalItem; }

    public void setTotalItem(Long totelItem) { this.totalItem = totelItem; }

    public Date getAddDate() { return addDate; }

    public void setAddDate(Date addDate) { this.addDate = addDate; }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getStatus() { return status; }

    public String getTag() { return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public void setStatus(Boolean status) { this.status = status;}
}
