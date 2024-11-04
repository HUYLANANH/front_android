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

public class Warning extends AppCompatActivity {
    ImageView img_dashboard, img_map, img_setting, img_back;
    Button btn_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_warning);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_warning), (v, insets) -> {
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

        img_dashboard = findViewById(R.id.img_dashboard);
        img_map = findViewById(R.id.img_map);
        img_setting = findViewById(R.id.img_setting);
        img_back = findViewById(R.id.img_back);
        btn_report = findViewById(R.id.btn_report);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}