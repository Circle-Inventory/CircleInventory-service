package com.toholanka.inventorybackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(generator = "user_id_generator")
    @GenericGenerator(
            name = "user_id_generator",
            strategy = "com.toholanka.inventorybackend.util.UserIdGenerator"
    )
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "imageUrl")
    private String imageUrl;

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
