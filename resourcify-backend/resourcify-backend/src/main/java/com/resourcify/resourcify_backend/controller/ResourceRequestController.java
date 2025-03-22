package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.ResourceRequestItem;
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
@CrossOrigin(origins = "http://127.0.0.1:5501")  // Match your frontend
public class ResourceRequestController {

    private final ResourceRepository resourceRepository;
    private final UserRequestRepository userRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Submit a resource request (deduct quantity and notify)
    @PostMapping("/submit")
    public ResponseEntity<String> submitRequest(@RequestBody UserRequest userRequest) {
        String resourceName = userRequest.getResourceName();
        String location = userRequest.getLocation();
        int quantityRequested = userRequest.getQuantity();

        Optional<ResourceItem> resourceItemOpt = resourceRepository.findByResourceNameAndLocation(resourceName, location);
        Optional<ResourceRequestItem> resourceRequestItemOpt = userRequestRepository.findByResourceNameAndLocation(resourceName, location);

        if (resourceItemOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Resource not found for given name and location.");
        }
        ResourceItem resourceItem = resourceItemOpt.get();
        ResourceRequestItem resourceRequestItem = resourceRequestItemOpt.get();

        // Validate quantity
        if (resourceRequestItem.getQuantity() < quantityRequested) {
            return ResponseEntity.badRequest().body("Not enough quantity available!");
        }

        // Deduct quantity
        resourceItem.setQuantity(resourceItem.getQuantity() - quantityRequested);
        resourceRepository.save(resourceItem);

        // Save user request
        userRequest.setResourceId(resourceItem.getId());
        userRequest.setStatus("Pending");
        userRequestRepository.save(userRequest);

        // Notify via WebSocket
        messagingTemplate.convertAndSend("/topic/updates", "Resource updated: " + resourceItem.getName());

        return ResponseEntity.ok("Request submitted successfully!");
    }

    // Get all requests (dashboard use)
    @GetMapping("/all")
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        List<UserRequest> requests = userRequestRepository.findAll();
        return ResponseEntity.ok(requests);
    }
}
