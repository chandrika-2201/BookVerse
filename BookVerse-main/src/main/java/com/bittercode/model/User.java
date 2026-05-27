package com.bittercode.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "username", length = 100)
    private String emailId; // maps to username column in db (primary key)

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "firstname", nullable = false, length = 100)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", nullable = false)
    private Long phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "mailid", nullable = false, length = 100)
    private String mailId;

    @Column(name = "usertype", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Constructors
    public User() {}

    public User(String emailId, String password, String firstName, String lastName, Long phone, String address, String mailId, UserRole role) {
        this.emailId = emailId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.mailId = mailId;
        this.role = role;
    }

    // Getters and Setters
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { 
        this.emailId = emailId; 
        this.mailId = emailId; 
    }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public Long getPhone() { return phone; }
    public void setPhone(Long phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getMailId() { return mailId; }
    public void setMailId(String mailId) { this.mailId = mailId; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
