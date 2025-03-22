package com.resourcify.resourcify_backend.repository;

import com.resourcify.resourcify_backend.model.ResourceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<ResourceItem, Integer> {

    @Query("SELECT DISTINCT r.location FROM ResourceItem r WHERE r.location IS NOT NULL")
    List<String> findDistinctLocations();

    Optional<ResourceItem> findByNameAndLocation(String name, String location);
}
