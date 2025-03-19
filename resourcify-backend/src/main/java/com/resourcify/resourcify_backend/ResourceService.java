package com.resourcify.resourcify_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
    
import java.util.List;

@Service   
public class ResourceService {

        @Autowired
        private ResourceRepository resourceRepository;
    
        public List<Resource> getAvailableItems() {
            return resourceRepository.findAll();
        }
    
        public boolean requestResource(String resourceName, int quantity) {
            Resource resource = resourceRepository.findByName(resourceName);
            if (resource != null && resource.getQuantity() >= quantity) {
                resource.setQuantity(resource.getQuantity() - quantity);
                resourceRepository.save(resource);
                return true;
            }
            return false;
        }
    }

