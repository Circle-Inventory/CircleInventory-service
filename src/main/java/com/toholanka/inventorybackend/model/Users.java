package com.toholanka.inventorybackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;

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

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "dateAdded")
    private Date addDate;

    @Column(name = "role")
    private String role;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "invite")
    private Boolean invite;

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Date getAddDate() { return addDate; }

    public void setAddDate(Date addDate) { this.addDate = addDate; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getInvite() { return invite; }

    public void setInvite(Boolean invite) { this.invite = invite; }
}
