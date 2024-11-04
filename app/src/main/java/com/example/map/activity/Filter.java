package com.example.map.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map.R;

public class Filter extends AppCompatActivity {
    ImageView img_back;
    Button btn_last_month, btn_last_week, btn_last_day, btn_at_this_time, btn_low, btn_medium, btn_high, btn_apply_filter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_filter), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hide the status bar and navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        // Apply WindowInsets to adjust padding when the navigation bar is shown/hidden
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {
            int bottomPadding = insets.getSystemWindowInsetBottom();
            findViewById(R.id.function).setPadding(0, 0, 0, bottomPadding);
            return insets.consumeSystemWindowInsets();
        });

        img_back = findViewById(R.id.img_back);
        btn_last_month = findViewById(R.id.btn_last_month);
        btn_last_week = findViewById(R.id.btn_last_week);
        btn_last_day = findViewById(R.id.btn_last_day);
        btn_at_this_time = findViewById(R.id.btn_at_this_time);
        btn_low = findViewById(R.id.btn_low);
        btn_apply_filter = findViewById(R.id.btn_apply_filter);
        btn_medium = findViewById(R.id.btn_medium);
        btn_high = findViewById(R.id.btn_high);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}