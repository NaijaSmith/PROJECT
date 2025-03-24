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
@RequestMapping("/api/resources")
@CrossOrigin(origins = "http://127.0.0.1:5501")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceRepository resourceRepository;
    private final UserRequestRepository userRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/available-items")
    public ResponseEntity<List<ResourceItem>> getAvailableItems() {
        return ResponseEntity.ok(resourceRepository.findAll());
    }

    @GetMapping("/location")
    public ResponseEntity<List<String>> getDistinctLocations() {
        return ResponseEntity.ok(resourceRepository.findDistinctLocations());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRequestStatus(
            @PathVariable int id,
            @RequestParam String status
    ) {
        Optional<UserRequest> optionalRequest = userRequestRepository.findById(id);

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserRequest userRequest = optionalRequest.get();
        userRequest.setStatus(status);
        userRequestRepository.save(userRequest);

        messagingTemplate.convertAndSend("/topic/requests", userRequest);

        if ("Approved".equalsIgnoreCase(status)) {
            messagingTemplate.convertAndSend("/topic/fulfilledRequests", userRequest);
        }

        return ResponseEntity.ok("Status updated to " + status);
    }
}
