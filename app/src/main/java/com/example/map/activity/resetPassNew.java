package com.example.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.map.R;
import com.example.map.client.ApiClient;
import com.example.map.client.ApiService;
import com.example.map.response.ResetPasswordResponse;
import com.example.map.request.ResetPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class resetPassNew extends AppCompatActivity {

    private EditText edtNewPassword, edtConfirmPassword, edtOtp;
    private Button btnConfirm;
    private String email;
    private String token;  // Lưu trữ token từ màn hình trước



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_new);

        // Lấy thông tin từ Intent
        email = getIntent().getStringExtra("email");
        token = getIntent().getStringExtra("token");

        // Ánh xạ các view
        edtNewPassword = findViewById(R.id.edt_new_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnConfirm = findViewById(R.id.btn_confirm);

        // Thiết lập sự kiện cho nút Confirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edtNewPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(resetPassNew.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(resetPassNew.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                resetPassword(newPassword, token);
            }
        });
    }


    // Hàm gọi API để reset mật khẩu
    private void resetPassword(String newPassword, String token) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        ResetPasswordRequest request = new ResetPasswordRequest(email, newPassword, token);

        // In log request
        Log.d("ResetPassword", "Request: " + "Email: " + email + ", NewPassword: " + newPassword + ", Token: " + token);

        Call<ResetPasswordResponse> call = apiService.resetPassword(request);
        call.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResetPasswordResponse apiResponse = response.body();

                    // In log kết quả API trả về
                    Log.d("ResetPassword", "API Response: " + apiResponse.getMessage());

                    // Kiểm tra nếu thông báo trả về từ API là "Đổi mật khẩu thành công"
                    if (apiResponse.getMessage().equals("Đổi mật khẩu thành công")) {
                        // Hiển thị thông báo thành công
                        Toast.makeText(resetPassNew.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        // Chuyển hướng đến màn hình login sau khi đổi mật khẩu thành công
                        Intent intent = new Intent(resetPassNew.this, Login.class);
                        startActivity(intent);

                        // Đóng màn hình hiện tại
                        finish();
                    } else {
                        // Nếu không phải "Đổi mật khẩu thành công", hiển thị thông báo thất bại
                        Toast.makeText(resetPassNew.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu API không trả về thông tin hợp lệ, hiển thị thông báo lỗi
                    Toast.makeText(resetPassNew.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                // In log lỗi kết nối
                Log.e("ResetPassword", "Error: " + t.getMessage());
                Toast.makeText(resetPassNew.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

