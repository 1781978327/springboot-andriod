package com.example.ai_1.network;

import com.example.ai_1.model.ChatRequest;
import com.example.ai_1.model.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/chat")
    Call<String> askQuestion(@Body ChatRequest request);

    @POST("api/users/register")
    Call<UserRequest> register(@Body UserRequest request);

    @POST("api/users/login")
    Call<UserRequest> login(@Body UserRequest request);
} 