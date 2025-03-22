package com.resourcify.resourcify_backend.repository;

import com.resourcify.resourcify_backend.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRequestRepository extends JpaRepository<UserRequest, Integer> {

    Optional<UserRequest> findByNameAndLocation(String name, String location);
}
