package com.example.map.client;

import com.example.map.model.Tile;
import com.example.map.model.WeatherForecast;
import com.example.map.request.LoginRequest;
import com.example.map.request.RegisterRequest;
import com.example.map.response.LoginResponse;
import com.example.map.response.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/Auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET("WeatherForecast")
    Call<List<WeatherForecast>> getWeatherForecast(@Header("Authorization") String token);

    @GET("api/Map/get-info")
    Call<List<Tile>> getAvailableTiles();
}

