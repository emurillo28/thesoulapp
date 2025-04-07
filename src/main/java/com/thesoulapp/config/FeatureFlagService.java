package com.thesoulapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {

    @Value("${feature.tasks.enabled:false}")
    private boolean tasksEnabled;

    @Value("${feature.blog.enabled:false}")
    private boolean blogEnabled;

    @Value("${feature.insights.enabled:false}")
    private boolean insightsEnabled;

    public boolean isTasksEnabled() {
        return tasksEnabled;
    }

    public boolean isBlogEnabled() {
        return blogEnabled;
    }

    public boolean isInsightsEnabled() {
        return insightsEnabled;
    }
} 