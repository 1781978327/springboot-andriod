package com.example.ai_1.network;

import com.example.ai_1.model.ChatRequest;
import com.example.ai_1.model.User;
import com.example.ai_1.model.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("ask")
    Call<String> askQuestion(@Body ChatRequest request);

    @POST("api/register")
    Call<User> register(@Body UserRequest request);

    @POST("api/login")
    Call<User> login(@Body UserRequest request);
} 