package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.UserRequest;
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
@CrossOrigin(origins = "http://127.0.0.1:5501") // Frontend origin
public class RequestController {

    private final UserRequestRepository userRequestRepository;
    private final SimpMessagingTemplate messagingTemplate; // for WebSocket push

    // 1. Get all requests (Dashboard)
    @GetMapping
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        List<UserRequest> requests = userRequestRepository.findAll();
        return ResponseEntity.ok(requests);
    }

    // 2. Get a single request by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserRequest> getRequestById(@PathVariable int id) {
        Optional<UserRequest> request = userRequestRepository.findById(id);
        return request.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // 3. Update request status (Fulfilled / Pending / Rejected)
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

        // WebSocket broadcast (optional)
        messagingTemplate.convertAndSend("/topic/requests", userRequest);

        return ResponseEntity.ok("Status updated to " + status);
    }

    // 4. Delete a request (optional)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable int id) {
        if (!userRequestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRequestRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/requests", "Request deleted: " + id);

        return ResponseEntity.ok("Request deleted successfully!");
    }
}
