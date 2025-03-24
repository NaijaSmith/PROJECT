package com.resourcify.resourcify_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501") // Frontend origin
public class PredictionController {

    private final PredictionService predictionService;

    /**
     * API endpoint to predict resource demand at a specific location.
     *
     * @param location Location to predict demand for
     * @param daysAhead Number of days ahead to predict (default: 7)
     * @return Predicted demand as JSON map of resource names and quantities
     */
    @GetMapping("/{location}")
    public ResponseEntity<Map<String, Integer>> predictDemand(
            @PathVariable String location,
            @RequestParam(defaultValue = "7") int daysAhead
    ) {
        Map<String, Integer> predictions = predictionService.predictDemand(location, daysAhead);

        if (predictions.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", 0));  // Return empty if no data found
        }

        return ResponseEntity.ok(predictions);
    }
}
