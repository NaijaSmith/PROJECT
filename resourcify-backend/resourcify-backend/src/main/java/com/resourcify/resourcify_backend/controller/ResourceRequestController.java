package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.ResourceItem;
import com.resourcify.resourcify_backend.model.UserRequest;
import com.resourcify.resourcify_backend.repository.ResourceRepository;
import com.resourcify.resourcify_backend.repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501") // Frontend origin
public class ResourceRequestController {

    private final ResourceRepository resourceRepository;
    private final UserRequestRepository userRequestRepository;
    private final SimpMessagingTemplate messagingTemplate; // For WebSocket messages

    @PostMapping("/submit")
    public ResponseEntity<String> submitRequest(@RequestBody UserRequest userRequest) {
        String resourceName = userRequest.getResourceName();
        String location = userRequest.getLocation();
        int quantityRequested = userRequest.getQuantity();

        // Step 1: Find the resource by name and location
        Optional<ResourceItem> resourceItemOpt = resourceRepository.findByNameAndLocation(resourceName, location);

        if (resourceItemOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Resource not found for given name and location.");
        }

        ResourceItem resourceItem = resourceItemOpt.get();

        // Step 2: Validate available quantity
        if (resourceItem.getQuantity() < quantityRequested) {
            return ResponseEntity.badRequest().body("Not enough quantity available!");
        }

        // Step 3: Deduct quantity and save updated resource
        resourceItem.setQuantity(resourceItem.getQuantity() - quantityRequested);
        resourceRepository.save(resourceItem);

        // Step 4: Save the request to the requests table
        userRequest.setResourceId(resourceItem.getId());
        userRequest.setStatus("Pending"); // or Approved, if automatic
        userRequestRepository.save(userRequest);

        // Step 5: Notify subscribers via WebSocket (optional but awesome!)
        messagingTemplate.convertAndSend("/topic/updates", "Resource updated: " + resourceItem.getName());

        return ResponseEntity.ok("Resource requested and updated successfully!");
    }
}
