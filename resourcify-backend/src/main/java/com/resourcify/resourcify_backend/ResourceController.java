package com.resourcify.resourcify_backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    @PostMapping
    public Resource createResource(@RequestBody Resource resource) {
        return resourceRepository.save(resource);
    }
}
