package com.example.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

//        String token = Token.getToken(this);
//        if (token != null) {
//            Log.d("TOKEN", "Token found: " + token);
//            fetchWeatherForecast(token);
//        } else {
//            Log.e("TOKEN_ERROR", "No token found, please login first.");
//        }
        ImageView mapIcon = findViewById(R.id.img_map);
        ImageView settingIcon = findViewById(R.id.img_setting);
        Button reportBtn = findViewById(R.id.report_issue_button);

        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dash.this, map.class);
                startActivity(intent);
            }
        });

        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dash.this, setting.class);
                startActivity(intent);
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dash.this, report.class);
                startActivity(intent);
            }
        });
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