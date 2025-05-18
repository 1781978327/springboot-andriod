package com.example.demo2.service;

import com.example.demo2.config.DeepSeekConfig;
import com.example.demo2.model.DeepSeekRequest;
import com.example.demo2.model.DeepSeekResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class DeepSeekService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DeepSeekConfig deepSeekConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String askDeepSeek(String question) throws JsonProcessingException {
        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel("deepseek-chat");
        request.setStream(false);

        List<DeepSeekRequest.Message> messages = List.of(
            new DeepSeekRequest.Message("user", question)
        );
        request.setMessages(messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(deepSeekConfig.getApiKey());

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deepSeekConfig.getApiUrl());

        ResponseEntity<String> response = restTemplate.postForEntity(builder.toUriString(), entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            DeepSeekResponse deepSeekResponse = objectMapper.readValue(response.getBody(), DeepSeekResponse.class);

            if (deepSeekResponse != null && deepSeekResponse.getChoices() != null && !deepSeekResponse.getChoices().isEmpty()) {
                return deepSeekResponse.getChoices().get(0).getMessage().getContent();
            }
        }

        return "No valid response from DeepSeek";
    }
} 