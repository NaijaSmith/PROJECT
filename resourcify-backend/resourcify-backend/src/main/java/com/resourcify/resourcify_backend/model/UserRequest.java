package com.resourcify.resourcify_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Resource_id", nullable = false)
    private int resourceId;

    @Column(name = "User_id", nullable = false)
    private int userId;

    @Column(name = "Status", nullable = false)
    private String status; // Fulfilled / Pending / Rejected
    

    @Column(name = "Request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(length = 255, nullable = false)
    private String location;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "resource_name", length = 255, nullable = false)
    private String resourceName;

    // Constructors
    public UserRequest() {
        this.requestDate = LocalDateTime.now();
        this.status = "Pending"; // Default status (optional)
    }

    public UserRequest(int resourceId, int userId, String resourceName, String location, int quantity, String status) {
        this.resourceId = resourceId;
        this.userId = userId;
        this.resourceName = resourceName;
        this.location = location;
        this.quantity = quantity;
        this.status = status;
        this.requestDate = LocalDateTime.now();
    }

    // Getters and Setters

    public int getId() { return id; }

    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

}
