package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.ResourceItem;
import com.resourcify.resourcify_backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources/view")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501") // specify frontend origin
public class ResourceViewController {

    private final ResourceRepository resourceRepository;

    /**
     * ✅ Get all available items for users
     * Endpoint: GET /api/resources/view/available-items
     */
    @GetMapping("/available-items")
    public ResponseEntity<List<ResourceItem>> getAvailableItems() {
        List<ResourceItem> items = resourceRepository.findAll();
        return ResponseEntity.ok(items);
    }

    /**
     * ✅ Get distinct locations for users
     * Endpoint: GET /api/resources/view/locations
     */
    @GetMapping("/locations")
    public ResponseEntity<List<String>> getLocations() {
        List<String> locations = resourceRepository.findDistinctLocations();
        return ResponseEntity.ok(locations);
    }
}
