package com.resourcify.resourcify_backend.controller;


import com.resourcify.resourcify_backend.model.UserRequest;
import com.resourcify.resourcify_backend.repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // For logging

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class RequestController {

    private final UserRequestRepository userRequestRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        List<UserRequest> requests = userRequestRepository.findAll();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequest> getRequestById(@PathVariable int id) {
        return userRequestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRequestStatus(
            @PathVariable int id,
            @RequestParam String status) {
        Optional<UserRequest> optionalRequest = userRequestRepository.findById(id);

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserRequest userRequest = optionalRequest.get();
        userRequest.setStatus(status);
        userRequestRepository.save(userRequest);
        messagingTemplate.convertAndSend("/topic/requests", userRequest);

        return ResponseEntity.ok("Status updated to " + status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRequest(@PathVariable int id) {
        if (!userRequestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRequestRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/requests", "Request deleted: " + id);

        return ResponseEntity.ok("Request deleted successfully!");
    }
}