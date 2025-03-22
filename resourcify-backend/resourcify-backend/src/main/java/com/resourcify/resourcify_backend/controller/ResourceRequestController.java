package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.ResourceItem;
import com.resourcify.resourcify_backend.model.UserRequest;
import com.resourcify.resourcify_backend.repository.ResourceRepository;
import com.resourcify.resourcify_backend.repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")  // Match your frontend URL
public class ResourceRequestController {

    private final ResourceRepository resourceRepository;
    private final UserRequestRepository userRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Submit a resource request:
     * 1. Check if the resource exists at the given location.
     * 2. Validate if there's enough quantity.
     * 3. Deduct the quantity and update the resource.
     * 4. Save the user request.
     * 5. Notify via WebSocket.
     */
    @PostMapping("/submit")
    public ResponseEntity<String> submitRequest(@RequestBody UserRequest userRequest) {

        String name = userRequest.getName();               // Resource name
        String location = userRequest.getLocation();       // Resource location
        int quantityRequested = userRequest.getQuantity(); // Quantity user is requesting

        // 1. Look up the resource by name and location
        Optional<ResourceItem> resourceItemOpt = resourceRepository.findByNameAndLocation(name, location);

        if (resourceItemOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Resource not found for the given name and location.");
        }

        ResourceItem resourceItem = resourceItemOpt.get();

        // 2. Validate if enough quantity is available
        if (resourceItem.getQuantity() < quantityRequested) {
            return ResponseEntity
                    .badRequest()
                    .body("Not enough quantity available! Requested: " + quantityRequested
                            + ", Available: " + resourceItem.getQuantity());
        }

        // 3. Deduct the requested quantity and save the updated resource
        int newQuantity = resourceItem.getQuantity() - quantityRequested;
        resourceItem.setQuantity(newQuantity);
        resourceRepository.save(resourceItem);

        // 4. Save the user request
        userRequest.setResourceId(resourceItem.getId()); // FK to resource item
        userRequest.setStatus("Pending");
        userRequestRepository.save(userRequest);

        // 5. Notify subscribers via WebSocket
        messagingTemplate.convertAndSend(
                "/topic/updates",
                "Resource updated: " + resourceItem.getName() + " | New quantity: " + newQuantity
        );

        return ResponseEntity.ok("Request submitted successfully!");
    }

    /**
     * Get all resource requests for dashboard/admin panel.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        List<UserRequest> requests = userRequestRepository.findAll();
        return ResponseEntity.ok(requests);
    }
}
