package com.example.demo.controller;

import com.example.demo.config.AppProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final AppProperties appProperties;

    public ConfigController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping
    public String showConfig() {
        return "Title: " + appProperties.getTitle() +
                ", Datasource: " + appProperties.getDatasource() +
                ", Debug Mode: " + appProperties.isDebugMode();
    }
}
