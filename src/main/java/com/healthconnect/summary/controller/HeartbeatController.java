package com.healthconnect.summary.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Heartbeat endpoint for health checks.
 */
@RestController
public class HeartbeatController {

    @GetMapping("/heartbeat")
    public ResponseEntity<Map<String, String>> heartbeat() {
        // Return a JSON object, not a raw string, for proper content negotiation
        return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }
}