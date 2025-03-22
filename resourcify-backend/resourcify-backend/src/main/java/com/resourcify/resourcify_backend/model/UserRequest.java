package com.resourcify.resourcify_backend.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "resource_id")
    private int resourceId;

    @Column(name = "user_id")
    private int userId;

    private String name;

    private String status;

    private String location;

    private int quantity;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    // Constructors
    public UserRequest() {
    }

    public UserRequest(int id, int resourceId, int userId, String name, String status, String location, int quantity, LocalDateTime requestDate) {
        this.id = id;
        this.resourceId = resourceId;
        this.userId = userId;
        this.name = name;
        this.status = status;
        this.location = location;
        this.quantity = quantity;
        this.requestDate = requestDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
