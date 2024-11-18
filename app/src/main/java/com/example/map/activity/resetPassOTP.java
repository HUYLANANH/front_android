package com.example.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.map.R;
import com.example.map.client.ApiService;
import com.example.map.client.ApiClient;
import com.example.map.request.OtpRequest;
import com.example.map.response.OtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class resetPassOTP extends AppCompatActivity {

    private EditText otpEditText;
    private Button btnConfirm, btnResend;
    private TextView emailDisplay, otpInstruction, timerTextView;
    private String email; // Email nhận từ Intent
    private int timeLeft = 30;  // Thời gian đếm ngược 30 giây
    private boolean isTimerRunning = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_otp);

        // Lấy email từ Intent
        email = getIntent().getStringExtra("email");

        // In log để xem email nhận được
        Log.d("OTPVerification", "Email từ Intent: " + email);

        // Khởi tạo các view
        otpEditText = findViewById(R.id.otp_edit_text);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnResend = findViewById(R.id.btn_resend);
        emailDisplay = findViewById(R.id.email_display);
        otpInstruction = findViewById(R.id.otp_instruction);
        timerTextView = findViewById(R.id.timer_text);

        // Hiển thị email lên UI
        if (email != null) {
            emailDisplay.setText(email);  // Hiển thị email trong TextView
        }

        // Xử lý sự kiện khi nhấn nút "Xác nhận"
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy mã OTP từ EditText
                String otp = otpEditText.getText().toString();

                // Kiểm tra nếu OTP có đủ 6 ký tự
                if (otp.length() == 6) {
                    // Gửi yêu cầu tới API để xác minh OTP
                    verifyOtp(email, otp); // Sử dụng email và OTP để xác minh
                } else {
                    Toast.makeText(resetPassOTP.this, "Vui lòng nhập mã OTP đầy đủ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút "Gửi lại mã"
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    resendOtp(email);  // Gửi lại OTP tới email
                    startCountdown();  // Bắt đầu đếm ngược 30 giây
                } else {
                    Toast.makeText(resetPassOTP.this, "Vui lòng đợi " + timeLeft + " giây nữa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyOtp(String email, String otp) {
        // In log để xem email và OTP
        Log.d("OTPVerification", "Email: " + email);
        Log.d("OTPVerification", "OTP: " + otp);

        // Tạo đối tượng request với email và OTP
        OtpRequest otpRequest = new OtpRequest(email, otp);

        // Gọi API xác minh OTP
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<OtpResponse> call = apiService.verifyOtp(otpRequest);

        // Thực hiện gọi API và xử lý phản hồi
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful()) {
                    // Kiểm tra phản hồi từ API
                    OtpResponse otpResponse = response.body();
                    if (otpResponse != null) {
                        String token = otpResponse.getToken();  // Lấy mã thông báo từ phản hồi
                        if (token != null) {
                            // Hiển thị thông báo và điều hướng người dùng đến trang thay đổi mật khẩu
                            Toast.makeText(resetPassOTP.this, "OTP xác minh thành công", Toast.LENGTH_SHORT).show();
                            // Chuyển đến màn hình thay đổi mật khẩu
                            Intent intent = new Intent(resetPassOTP.this, resetPassNew.class);
                            intent.putExtra("email", email);  // Truyền email sang màn hình resetPassNew
                            intent.putExtra("token", token);  // Truyền token sang màn hình resetPassNew
                            startActivity(intent);
                        }
                    }
                } else {
                    // Xử lý lỗi khi API trả về thất bại
                    String errorMessage = "Lỗi khi xác minh OTP";
                    Toast.makeText(resetPassOTP.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(resetPassOTP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOtp(String email) {
        // Gọi API quên mật khẩu gửi lại OTP
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.forgotPassword1(new OtpRequest(email, "")); // Thực tế không cần OTP ở đây, chỉ cần email

        // Gửi lại yêu cầu API
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(resetPassOTP.this, "Mã OTP đã được gửi lại đến email của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(resetPassOTP.this, "Lỗi khi gửi lại OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(resetPassOTP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountdown() {
        isTimerRunning = true;
        btnResend.setEnabled(false);  // Vô hiệu hóa nút "Gửi lại mã"

        // Cập nhật TextView mỗi giây
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerTextView.setText("Còn lại: " + timeLeft + "s");
                    handler.postDelayed(this, 1000);  // Đếm lại mỗi giây
                } else {
                    btnResend.setEnabled(true);  // Kích hoạt lại nút sau 30 giây
                    timerTextView.setText("");  // Xóa TextView hiển thị thời gian
                    isTimerRunning = false;
                    timeLeft = 30;  // Đặt lại thời gian còn lại
                }
            }
        }, 1000);
    }
}
