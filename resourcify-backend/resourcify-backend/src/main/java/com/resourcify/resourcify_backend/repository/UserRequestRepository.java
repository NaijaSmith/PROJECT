package com.resourcify.resourcify_backend.repository;

import com.resourcify.resourcify_backend.model.UserRequest;
import com.resourcify.resourcify_backend.model.ResourceRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRequestRepository extends JpaRepository<UserRequest, Integer> {
    Optional<ResourceRequestItem> findByResourceNameAndLocation(String resourceName, String location);
}