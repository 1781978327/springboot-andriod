package com.example.demo2.controller;

import com.example.demo2.model.ChatRequest;
import com.example.demo2.service.DeepSeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class DeepSeekController {

    @Autowired
    private DeepSeekService deepSeekService;

    @PostMapping("/ask")
    public ResponseEntity<String> askDeepSeek(@RequestBody ChatRequest request) {
        try {
            String response = deepSeekService.askDeepSeek(request.getQuestion());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred while communicating with DeepSeek: " + e.getMessage());
        }
    }
} 