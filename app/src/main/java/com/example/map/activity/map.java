package com.example.map.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.map.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;


public class map extends AppCompatActivity {
    ImageView warningIcon, filterIcon, dashboardIcon, mapIcon, settingIcon;
    private MapView map;
    private static final int REQUEST_PERMISSIONS = 1;
    Double latCurrentPosition = 0.0, longCurrentPosition = 0.0;
    //private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        // Check for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_PERMISSIONS);
        } else {
            // If permission has already been granted, you can load tiles or set up other functionalities here
            //loadOfflineTiles();
        }


        warningIcon = findViewById(R.id.img_warning);
        filterIcon = findViewById(R.id.img_filter);
        dashboardIcon = findViewById(R.id.img_dashboard);
        mapIcon = findViewById(R.id.img_map);
        settingIcon = findViewById(R.id.img_setting);
        map = findViewById(R.id.offline_map);
        //searchView = findViewById(R.id.search_location);

        warningIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(map.this, Warning.class);
                startActivity(intent);
            }
        });

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

        IMapController mapController = map.getController();
        mapController.setZoom(15.0);
        GeoPoint geoPoint = new GeoPoint(10.878488618182582, 106.8063226828715);
        mapController.setCenter(geoPoint);

        //configureOSMDroid();
        //initializeMap();
    }

    private void configureOSMDroid(){
        // Load the OSMDroid configuration
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));

        // Set up cache directory (external storage or internal storage)
        File osmdroidBasePath = getExternalFilesDir("osmdroid");
        Configuration.getInstance().setOsmdroidBasePath(osmdroidBasePath);
        Configuration.getInstance().setOsmdroidTileCache(new File(osmdroidBasePath, "tiles"));
    }

    private void initializeMap(){
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Enable zoom controls
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);

        final MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationNewOverlay.enableMyLocation();
        map.getOverlays().add(myLocationNewOverlay);
        myLocationNewOverlay.runOnFirstFix(() -> {
            if (myLocationNewOverlay.getMyLocation() != null){
                runOnUiThread(() -> {
                    map.getController().setCenter(myLocationNewOverlay.getMyLocation());
                    latCurrentPosition = myLocationNewOverlay.getMyLocation().getLatitude();
                    longCurrentPosition = myLocationNewOverlay.getMyLocation().getLongitude();
                });
            }
        });
        GeoPoint geoPoint = new GeoPoint(10.878488618182582, 106.8063226828715);
        Marker myMakerPoint = new Marker(map);
        myMakerPoint.setPosition(geoPoint);
        myMakerPoint.setTitle("KTX Khu A, Đại học Quốc gia Thành phố Hồ Chí Minh");
        myMakerPoint.setIcon(this.getResources().getDrawable(R.drawable.img_placeholder));
        myMakerPoint.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(myMakerPoint);

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint point) {
                Toast.makeText(getApplicationContext(), "Single tap at: " + point.getLatitude() + ", " + point.getLongitude(), Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint point) {
                Toast.makeText(getApplicationContext(), "Long press at: " + point.getLatitude() + ", " + point.getLongitude(), Toast.LENGTH_SHORT).show();
                return false;
            }
        };
    }

    private void loadOfflineTiles() {
        // Checking network state to determine online/offline mode
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            networkInfo = cm.getActiveNetworkInfo();
        } else {
            // You may want to handle the lack of permission here accordingly
            networkInfo = null;
        }

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Load online tiles if connected
            map.setTileSource(TileSourceFactory.MAPNIK);
            Toast.makeText(this, "You are online. Using online maps.", Toast.LENGTH_SHORT).show();
        } else {
            // Offline mode
            map.setTileSource(TileSourceFactory.MAPNIK);
            Toast.makeText(this, "You are offline. Loading cached map tiles.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the map view
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the map view
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup
        map.onDetach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the permissions were granted
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                loadOfflineTiles();
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}