package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.ResourceItem;
import com.resourcify.resourcify_backend.model.ResourceRequest;
import com.resourcify.resourcify_backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceRepository resourceRepository;

    @GetMapping("/get-available-items")
    public ResponseEntity<List<ResourceItem>> getAvailableItems() {
        List<ResourceItem> items = resourceRepository.findAll();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/location")
    public ResponseEntity<List<String>> getLocation() {
        List<String> location = resourceRepository.findAllDistinctLocations();
        return ResponseEntity.ok(location);
    }

    @PostMapping("/request-resource")
    public ResponseEntity<String> requestResource(@RequestBody ResourceRequest request) {
        // Business logic here (e.g., validate, update DB)
        System.out.println("Received request: " + request);
        return ResponseEntity.ok("Request submitted successfully!");
    }
}
