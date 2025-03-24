package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.UserRequest;
import com.resourcify.resourcify_backend.repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component  // Use @Component since it's under controller, but ideally @Service
@RequiredArgsConstructor
public class PredictionService {

    private final UserRequestRepository userRequestRepository;

    /**
     * Predict demand for resources at a given location.
     *
     * @param location Location to predict demand for
     * @param daysAhead How many days ahead to predict (scaling factor)
     * @return Map of resource names and their predicted demand quantities
     */
    public Map<String, Integer> predictDemand(String location, int daysAhead) {

        // Filter: last 30 days requests by location
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        List<UserRequest> recentRequests = userRequestRepository
                .findByLocationAndRequestDateAfter(location, thirtyDaysAgo);

        if (recentRequests.isEmpty()) {
            return Collections.emptyMap();  // No data, return empty prediction
        }

        // Aggregate quantities by resource name
        Map<String, List<Integer>> resourceQuantities = recentRequests.stream()
                .collect(Collectors.groupingBy(
                        UserRequest::getName,
                        Collectors.mapping(UserRequest::getQuantity, Collectors.toList())
                ));

        // Calculate average and project prediction
        Map<String, Integer> predictions = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : resourceQuantities.entrySet()) {
            String resourceName = entry.getKey();
            List<Integer> quantities = entry.getValue();

            int averageDemand = (int) quantities.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0);

            // Simple scale-up for prediction (adjust algorithm as needed)
            predictions.put(resourceName, averageDemand * daysAhead);
        }

        return predictions;
    }
}
