package com.resourcify.resourcify_backend;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;






@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/get-available-items")
    public List<Resource> getAvailableItems() {
        return resourceService.getAvailableItems();
    }

    @PostMapping("/request-resource")
    public ResponseEntity<String> requestResource(@RequestBody ResourceRequest request) {
        boolean success = resourceService.requestResource(request.getResourceName(), request.getQuantity());
        if (success) {
            return ResponseEntity.ok("Resource requested successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Resource not available or insufficient quantity");
        }
    }
}
