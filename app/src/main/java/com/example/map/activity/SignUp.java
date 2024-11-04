package com.example.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.map.R;
import com.example.map.client.ApiClient;
import com.example.map.client.ApiService;
import com.example.map.request.RegisterRequest;
import com.example.map.response.RegisterResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView = findViewById(R.id.term);
        String text = "By signing up, you agree to our <b><font color='#06066b'>Term and Conditions</font></b> and <b><font color='#06066b'>Privacy Policy</font></b>.";
        textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));

        hideSystemUI();

        Button signupButton = findViewById(R.id.signInButton);
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

        EditText usernameEditText = findViewById(R.id.usernameInput);
        EditText emailEditText = findViewById(R.id.EmailInput);
        EditText passwordEditText = findViewById(R.id.PassInput);
        EditText passwordConfirm = findViewById(R.id.ConfirmInput);

        Button loginbtn = findViewById(R.id.signupButton);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String pass_confirm = passwordConfirm.getText().toString();
                if(pass_confirm.equals(password))
                {
                    registerUser(username,email, password);
                }
                else{
                    Log.e("REGISTER_FAILED", "Not equal pass confirm");
                }
            }
        });
    }
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    public void registerUser(String username, String email, String password) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        Call<RegisterResponse> call = apiService.register(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() ) {
                    String responseBody = response.body().getMessage();
                    Log.d("REGISTER_SUCCESS", responseBody);
                    Intent intent = new Intent(SignUp.this, Login.class);
                    startActivity(intent);
                } else {
                    try {
                        Log.e("REGISTER_FAILED", "Registration failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("REGISTER_FAILED", "Error reading error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("REGISTER_ERROR", t.getMessage());
            }
        });
    }
}