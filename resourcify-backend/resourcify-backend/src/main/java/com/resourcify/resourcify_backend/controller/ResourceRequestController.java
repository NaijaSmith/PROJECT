package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.ResourceItem;
import com.resourcify.resourcify_backend.model.UserRequest;
import com.resourcify.resourcify_backend.repository.ResourceRepository;
import com.resourcify.resourcify_backend.repository.UserRequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ResourceRequestController {

    private final ResourceRepository resourceRepository;
    private final UserRequestRepository userRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * ✅ Submit a resource request (Users only)
     */
    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")  // Only users can submit requests
    public ResponseEntity<String> submitRequest(@RequestBody UserRequest userRequest) {
        log.info("Received resource request: {}", userRequest);

        String resourceName = userRequest.getName();
        String location = userRequest.getLocation();
        int quantityRequested = userRequest.getQuantity();

        if (resourceName == null || resourceName.isBlank() || location == null || location.isBlank()) {
            log.warn("Invalid request. Resource name or location is missing.");
            return ResponseEntity.badRequest().body("Resource name and location are required.");
        }

        userRequest.setUserId(1);
        userRequest.setRequestDate(LocalDateTime.now());
        userRequest.setStatus("Pending");

        Optional<ResourceItem> resourceItemOpt = resourceRepository.findByNameAndLocation(resourceName, location);
        if (resourceItemOpt.isEmpty()) {
            log.warn("Resource not found: {} at {}", resourceName, location);
            return ResponseEntity.badRequest().body("Resource not found for the given name and location.");
        }

        ResourceItem resourceItem = resourceItemOpt.get();
        if (resourceItem.getQuantity() < quantityRequested) {
            String message = String.format("Not enough quantity available! Requested: %d, Available: %d",
                    quantityRequested, resourceItem.getQuantity());
            log.warn(message);
            return ResponseEntity.badRequest().body(message);
        }

        resourceItem.setQuantity(resourceItem.getQuantity() - quantityRequested);
        resourceRepository.save(resourceItem);
        log.info("Resource updated: {} | New quantity: {}", resourceItem.getName(), resourceItem.getQuantity());

        userRequest.setResourceId(resourceItem.getId());
        userRequestRepository.save(userRequest);
        log.info("User request saved successfully: {}", userRequest);

        messagingTemplate.convertAndSend("/topic/updates",
                "Resource updated: " + resourceItem.getName() + " | New quantity: " + resourceItem.getQuantity());

        return ResponseEntity.ok("Request submitted successfully!");
    }

    /**
     * ✅ Get all resource requests (Admin only)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can view all requests
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        List<UserRequest> requests = userRequestRepository.findAll();
        log.info("Fetched {} user requests", requests.size());
        return ResponseEntity.ok(requests);
    }
}
