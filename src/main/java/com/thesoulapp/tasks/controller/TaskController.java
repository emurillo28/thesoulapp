package com.thesoulapp.tasks.controller; // New package example

import com.thesoulapp.config.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final FeatureFlagService featureFlagService;

    private void checkTaskFeatureEnabled() {
        if (!featureFlagService.isTasksEnabled()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks feature is not enabled");
        }
    }

    // Example placeholder endpoints
    @GetMapping
    public ResponseEntity<?> getAssignedTasks() {
        checkTaskFeatureEnabled();
        // Actual implementation would go here
        return ResponseEntity.ok("Task list would be here"); 
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> markTaskComplete(@PathVariable Long taskId) {
        checkTaskFeatureEnabled();
        // Actual implementation would go here
        return ResponseEntity.ok("Task completion logic would be here"); 
    }
} 