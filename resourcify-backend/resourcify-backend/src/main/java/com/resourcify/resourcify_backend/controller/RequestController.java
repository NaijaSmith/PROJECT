package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.UserRequest;
import com.resourcify.resourcify_backend.model.ResourceItem;
import com.resourcify.resourcify_backend.repository.UserRequestRepository;
import com.resourcify.resourcify_backend.repository.ResourceRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class RequestController {

    private final UserRequestRepository userRequestRepository;
    private final ResourceRepository resourceRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ResponseEntity<List<UserRequestDTO>> getAllRequests() {
        List<UserRequest> requests = userRequestRepository.findAll();

        List<UserRequestDTO> requestDTOs = requests.stream().map(request -> {
            ResourceItem resource = resourceRepository.findById(request.getResourceId()).orElse(null);
            String resourceName = (resource != null) ? resource.getName() : "Unknown Resource";
            return new UserRequestDTO(request, resourceName);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(requestDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequest> getRequestById(@PathVariable int id) {
        Optional<UserRequest> request = userRequestRepository.findById(id);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

        return ResponseEntity.ok("Status updated to " + status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable int id) {
        if (!userRequestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRequestRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/requests", "Request deleted: " + id);

        return ResponseEntity.ok("Request deleted successfully!");
    }

    @Data
    @AllArgsConstructor
    static class UserRequestDTO {
        private UserRequest request;
        private String resourceName;
    }
}