package com.example.ai_1.network;

import com.example.ai_1.model.ChatRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
        "Content-Type: application/json",
        "Accept: text/plain"
    })
    @POST("ask")
    Call<String> askQuestion(@Body ChatRequest request);
} 