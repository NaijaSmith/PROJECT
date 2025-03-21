package com.resourcify.resourcify_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resources") 
public class ResourceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private int quantity;
    private String location;
    private Double latitude; 
    private Double longitude; 

    // Constructors
    public ResourceItem() {}

    public ResourceItem(int id, String name, String description, int quantity, String location, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

}
