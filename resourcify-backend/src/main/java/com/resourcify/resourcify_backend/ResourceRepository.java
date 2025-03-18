package com.resourcify.resourcify_backend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // Custom queries can go here
}

