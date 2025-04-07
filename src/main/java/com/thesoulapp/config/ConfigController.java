package com.thesoulapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    @Value("${app.websocket.url}")
    private String websocketUrl;

    @Value("${feature.tasks.enabled}")
    private boolean tasksEnabled;

    @Value("${feature.blog.enabled}")
    private boolean blogEnabled;

    @Value("${feature.insights.enabled}")
    private boolean insightsEnabled;

    @GetMapping
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("apiBaseUrl", apiBaseUrl);
        config.put("websocketUrl", websocketUrl);
        config.put("features", Map.of(
            "tasks", tasksEnabled,
            "blog", blogEnabled,
            "insights", insightsEnabled
        ));
        return config;
    }
} 