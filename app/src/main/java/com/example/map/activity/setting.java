package com.example.map.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.activity.OnBackPressedCallback;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map.R;

public class setting extends AppCompatActivity
{

    private SeekBar volumeSeekBar;
    private AudioManager audioManager;
    private Switch vibrationSwitch;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.setting), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Switch switchToggle = findViewById(R.id.sensitive_switch);
        LinearLayout textContainer = findViewById(R.id.textContainer);
        SeekBar seekBar = findViewById(R.id.seekBar);

        switchToggle.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                textContainer.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
            } else
            {
                textContainer.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
            }
        });

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Thiết lập SeekBar
        volumeSeekBar = findViewById(R.id.sound_seek_bar);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        // Đặt mức âm lượng mặc định là 100% (tức là maxVolume)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

        // Thiết lập giá trị tối đa và giá trị hiện tại cho SeekBar
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(maxVolume);

        // Lắng nghe sự thay đổi của SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }
        });

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        // Khởi tạo Switch
        vibrationSwitch = findViewById(R.id.vibration_switch);

        // Kiểm tra hỗ trợ chế độ rung
        if (vibrator != null && vibrator.hasVibrator())
        {
            vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Kích hoạt chế độ rung (rung trong 500ms)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(500);
                        }
                    } else {
                        // Tắt rung
                        vibrator.cancel();
                    }
                }
            });
        } else {
            Log.e("Vibration", "Vibrator service is null");
        }

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
}