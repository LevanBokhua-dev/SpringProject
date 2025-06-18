package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MetricsController {

    @GetMapping("/metrics-dashboard")
    public String showDashboard() {
        return "metrics-dashboard"; // this will render metrics-dashboard.html from templates/
    }
}
