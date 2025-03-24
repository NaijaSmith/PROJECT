package com.resourcify.resourcify_backend.repository;

import com.resourcify.resourcify_backend.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRequestRepository extends JpaRepository<UserRequest, Integer> {

    Optional<UserRequest> findByNameAndLocation(String name, String location);

    static Optional<UserRequest> findById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    List<UserRequest> findByLocationAndRequestDateAfter(String location, LocalDate thirtyDaysAgo);
}
