package com.example.map.request;

public class ResetPasswordRequest {
    private String email;
    private String newPassword;
    private String token;

    public ResetPasswordRequest(String email, String newPassword, String token) {
        this.email = email;
        this.newPassword = newPassword;
        this.token = token;
    }

    // Getter và Setter
}
