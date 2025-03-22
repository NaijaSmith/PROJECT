package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.ResourceItem;
import com.resourcify.resourcify_backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources") // consistent resource path
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ResourceController {

    private final ResourceRepository resourceRepository;

    // ✅ Get all available items
    @GetMapping("/available-items")
    public ResponseEntity<List<ResourceItem>> getAvailableItems() {
        List<ResourceItem> items = resourceRepository.findAll();
        return ResponseEntity.ok(items);
    }

    // ✅ Get distinct locations from resources
    @GetMapping("/locations")
    public ResponseEntity<List<String>> getDistinctLocations() {
        List<String> locations = resourceRepository.findDistinctLocations();
        return ResponseEntity.ok(locations);
    }
}
