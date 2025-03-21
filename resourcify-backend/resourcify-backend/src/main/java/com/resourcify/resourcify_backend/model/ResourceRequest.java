package com.resourcify.resourcify_backend.model;

public class ResourceRequest {

    private String resourceName;
    private int quantity;

    // Constructors
    public ResourceRequest() {
    }

    public ResourceRequest(String resourceName, int quantity) {
        this.resourceName = resourceName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Object getLocation() {
        throw new UnsupportedOperationException("Unimplemented method 'getLocation'");
    }
}
