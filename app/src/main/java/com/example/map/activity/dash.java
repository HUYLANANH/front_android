package com.example.map.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map.R;
import com.example.map.client.ApiClient;
import com.example.map.client.ApiService;
import com.example.map.client.Token;
import com.example.map.model.WeatherForecast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String token = Token.getToken(this);
        if (token != null) {
            Log.d("TOKEN", "Token found: " + token);
            fetchWeatherForecast(token);
        } else {
            Log.e("TOKEN_ERROR", "No token found, please login first.");
        }
    }

    public void fetchWeatherForecast(String token)
    {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<List<WeatherForecast>> call = apiService.getWeatherForecast(token);
        call.enqueue(new Callback<List<WeatherForecast>>() {
            @Override
            public void onResponse(Call<List<WeatherForecast>> call, Response<List<WeatherForecast>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WeatherForecast> forecasts = response.body();
                    for (WeatherForecast forecast : forecasts) {
                        Log.d("WEATHER", "Date: " + forecast.getDate() + ", Temp: " + forecast.getTemperatureC());
                    }
                } else {
                    Log.e("WEATHER_FAILED", "Failed to fetch weather data");
                }
            }

            @Override
            public void onFailure(Call<List<WeatherForecast>> call, Throwable t) {
                Log.e("WEATHER_ERROR", t.getMessage());
            }
        });
    }
}