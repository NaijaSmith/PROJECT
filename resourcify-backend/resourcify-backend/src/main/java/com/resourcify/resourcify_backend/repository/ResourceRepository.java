package com.resourcify.resourcify_backend.repository;

import com.resourcify.resourcify_backend.model.ResourceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<ResourceItem, Integer> {

    @Query("SELECT DISTINCT r.location FROM ResourceItem r")
    List<String> findAllDistinctLocations();
}
