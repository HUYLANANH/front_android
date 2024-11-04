package com.example.map.client;

import android.content.Context;
import android.content.SharedPreferences;

public class Token {
    public static void saveToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TOKEN", token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        return preferences.getString("TOKEN", null);
    }
    public static void clearToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("TOKEN");
        editor.apply();
    }

}
