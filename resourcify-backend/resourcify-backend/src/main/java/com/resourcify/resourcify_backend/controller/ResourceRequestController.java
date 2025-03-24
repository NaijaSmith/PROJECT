package com.resourcify.resourcify_backend.controller;



import com.resourcify.resourcify_backend.model.ResourceItem;

import com.resourcify.resourcify_backend.model.UserRequest;

import com.resourcify.resourcify_backend.repository.ResourceRepository;

import com.resourcify.resourcify_backend.repository.UserRequestRepository;



import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j; // For logging



import org.springframework.http.ResponseEntity;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.web.bind.annotation.*;



import java.time.LocalDateTime;

import java.util.List;

import java.util.Optional;



@Slf4j

@RestController

@RequestMapping("/api/requests")

@RequiredArgsConstructor

@CrossOrigin(origins = "http://127.0.0.1:5501")

public class ResourceRequestController {



    private final ResourceRepository resourceRepository;

    private final UserRequestRepository userRequestRepository;

    private final SimpMessagingTemplate messagingTemplate;



    /**

     * Submit a resource request:

     * - Validate inputs and resource availability

     * - Deduct from available resources

     * - Save the request

     * - Notify clients via WebSocket

     */

 @PostMapping("/submit")

    public ResponseEntity<String> submitRequest(@RequestBody UserRequest userRequest) {

        log.info("Received resource request: {}", userRequest);



        String resourceName = userRequest.getName();

        String location = userRequest.getLocation();

        int quantityRequested = userRequest.getQuantity();



        // Validate input

        if (resourceName == null || resourceName.isBlank() || location == null || location.isBlank()) {

            log.warn("Invalid request. Resource name or location is missing.");

            return ResponseEntity.badRequest().body("Resource name and location are required.");

        }



        // Set static userId (replace with actual userId in a real app)

        userRequest.setUserId(1);

        userRequest.setRequestDate(LocalDateTime.now());

        userRequest.setStatus("Pending");



        // Look up resource item

        Optional<ResourceItem> resourceItemOpt = resourceRepository.findByNameAndLocation(resourceName, location);



        if (resourceItemOpt.isEmpty()) {

            log.warn("Resource not found: {} at {}", resourceName, location);

            return ResponseEntity.badRequest().body("Resource not found for the given name and location.");

        }



        ResourceItem resourceItem = resourceItemOpt.get();



        // Validate quantity

        if (resourceItem.getQuantity() < quantityRequested) {

            String message = String.format("Not enough quantity available! Requested: %d, Available: %d",

                    quantityRequested, resourceItem.getQuantity());

            log.warn(message);

            return ResponseEntity.badRequest().body(message);

        }



        // Deduct quantity

        resourceItem.setQuantity(resourceItem.getQuantity() - quantityRequested);

        resourceRepository.save(resourceItem);

        log.info("Resource updated: {} | New quantity: {}", resourceItem.getName(), resourceItem.getQuantity());



        // Save user request

        userRequest.setResourceId(resourceItem.getId());

        userRequestRepository.save(userRequest);

        log.info("User request saved successfully: {}", userRequest);



        // Notify via WebSocket

        messagingTemplate.convertAndSend("/topic/updates",

                "Resource updated: " + resourceItem.getName() + " | New quantity: " + resourceItem.getQuantity());



        return ResponseEntity.ok("Request submitted successfully!");

    }



    /**

     * Get all resource requests for dashboard/admin panel.

     */

    @GetMapping("/all")

    public ResponseEntity<List<UserRequest>> getAllRequests() {

        List<UserRequest> requests = userRequestRepository.findAll();

        log.info("Fetched {} user requests", requests.size());

return ResponseEntity.ok(requests);

    }

}