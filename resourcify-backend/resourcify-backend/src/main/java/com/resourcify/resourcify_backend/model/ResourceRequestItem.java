package com.resourcify.resourcify_backend.model;

public class ResourceRequestItem {

    private int id;
    private int resourceId;
    private String name;
    private String status;
    private int quantity;
    private String location;

    // Constructors
    public ResourceRequestItem() {
    }

    public ResourceRequestItem(int id, int resourceId, String status, int quantity, String location, String name) {
            this.id = id;
            this.resourceId = resourceId;
            this.name = name;
        this.status = status;
        this.quantity = quantity;
        this.location = location;
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

    public String getName() {
        return name;
    }

    public void Name(String Name) {
        this.name = Name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
