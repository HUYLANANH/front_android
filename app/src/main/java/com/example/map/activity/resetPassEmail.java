package com.example.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.map.R;
import com.example.map.client.ApiClient;
import com.example.map.client.ApiService;
import com.example.map.request.EmailRequest;
import com.example.map.response.EmailResponse;
import com.example.map.activity.resetPassOTP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class resetPassEmail extends AppCompatActivity {

    private TextView textCancel, textGoogleAccount, textEmailRecovery;
    private EditText emailInputEditText;
    private Button continueButton;

    private LinearLayout layoutGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_email); // Đảm bảo rằng bạn đã đặt đúng layout XML

        // Ánh xạ các phần tử UI từ XML
        textCancel = findViewById(R.id.text_cancel);
        textGoogleAccount = findViewById(R.id.text_google_account);
        textEmailRecovery = findViewById(R.id.text_email_recovery);
        emailInputEditText = findViewById(R.id.edit_email_input);
        continueButton = findViewById(R.id.button_continue);
        layoutGoogleLogin = findViewById(R.id.layout_google_login);


        // Cài đặt sự kiện cho nút "Tiếp tục"
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInputEditText.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(resetPassEmail.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(email); // Gọi API để reset mật khẩu
                }
            }
        });

        // Cài đặt sự kiện cho nút "Hủy"
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity khi người dùng nhấn "Hủy"
            }
        });

        // Nếu bạn muốn thêm hành động cho phần Google Login
        layoutGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bạn có thể thực hiện Google login tại đây
                Toast.makeText(resetPassEmail.this, "Đang đăng nhập bằng Google...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm để gửi yêu cầu reset mật khẩu tới API
    private void resetPassword(String email) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        EmailRequest emailRequest = new EmailRequest(email);

        Call<EmailResponse> call = apiService.resetPassEmail(emailRequest);
        call.enqueue(new Callback<EmailResponse>() {
            @Override
            public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(resetPassEmail.this, "Yêu cầu khôi phục mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng người dùng sang màn hình OTP để nhập mã OTP
                    Intent intent = new Intent(resetPassEmail.this, resetPassOTP.class);
                    intent.putExtra("email", email);
                    startActivity(intent);

                } else {
                    Toast.makeText(resetPassEmail.this, "Khôi phục mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<EmailResponse> call, Throwable t) {
                Toast.makeText(resetPassEmail.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
