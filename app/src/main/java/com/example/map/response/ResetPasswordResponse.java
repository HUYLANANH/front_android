package com.example.map.response;

import java.util.List;

public class ResetPasswordResponse {
    private String message;
    private List<String> errors;

    // Constructor
    public ResetPasswordResponse(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    // Getter và Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
