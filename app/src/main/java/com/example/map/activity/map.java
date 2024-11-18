package com.example.map.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map.R;
import com.example.map.client.ApiClient;
import com.example.map.client.ApiService;
import com.example.map.client.Token;
import com.example.map.mapservice.CustomTilesProvider;
import com.example.map.model.Tile;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class map extends AppCompatActivity {
    ImageView warningIcon, filterIcon, dashboardIcon, mapIcon, settingIcon;
    private MapView mapView;
    private Set<Tile> availableTiles = new HashSet<>();
    private CustomTilesProvider customTileProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        // Set up edge-to-edge window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map), (v, insets) -> {
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

        warningIcon = findViewById(R.id.img_warning);
        filterIcon = findViewById(R.id.img_filter);
        dashboardIcon = findViewById(R.id.img_dashboard);
        mapIcon = findViewById(R.id.img_map);
        settingIcon = findViewById(R.id.img_setting);
        mapView = findViewById(R.id.offline_map);
        mapView.setTileSource(createTileSource());

        fetchAvailableTiles();

        //set button warning
        warningIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(map.this, Warning.class);
                startActivity(intent);
            }
        });

        //set button filter
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(map.this, Filter.class);
                startActivity(intent);
            }
        });

        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(map.this, setting.class);
                startActivity(intent);
            }
        });

        dashboardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(map.this, dash.class);
                startActivity(intent);
            }
        });
    }

    private void fetchAvailableTiles() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.getAvailableTiles().enqueue(new Callback<List<Tile>>() {
            @Override
            public void onResponse(Call<List<Tile>> call, Response<List<Tile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    availableTiles.addAll(response.body());
                    Log.d("Tiles", "Available tiles: " + availableTiles);
                    setupMap(); // Sau khi có dữ liệu, tiến hành cấu hình MapView
                } else {
                    Log.e("Tiles", "Failed to fetch tiles: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Tile>> call, Throwable t) {

            }
        });
    }

    private XYTileSource createTileSource() {
        // Tạo một tile source mới cho MapView
        return new XYTileSource(
                "CustomTiles",       // Tên của tile source
                11,                  // Zoom level nhỏ nhất
                14,                  // Zoom level lớn nhất
                256,                 // Kích thước của mỗi tile (pixel)
                ".png",              // Định dạng file
                new String[]{"http://10.0.3.2:5122/api/Map/"} // URL backend
        );
    }

    private void setupMap() {
        // Tạo CustomTileProvider với danh sách tile hợp lệ
        customTileProvider = new CustomTilesProvider(getApplicationContext(), createTileSource(), availableTiles);

        // Gán CustomTileProvider cho MapView
        mapView.setTileProvider(customTileProvider);

        // Cấu hình các tính năng của MapView
        mapView.setMaxZoomLevel(14.0);           // Zoom tối đa
        mapView.setMinZoomLevel(11.0);           // Zoom tối thiểu

        // Thiết lập vị trí mặc định của bản đồ (ví dụ: trung tâm)
        IMapController mapController = mapView.getController();
        mapController.setZoom(12);             // Set zoom level
    }
}