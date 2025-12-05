package com.example.md_08_ungdungfivestore.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    // Tên file SharedPreferences và key phải khớp chính xác!
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String TOKEN_KEY = "token";

    // --- HÀM LƯU TOKEN (Cần thiết) ---
    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply(); // Lưu trữ bất đồng bộ
    }

    // --- HÀM LẤY TOKEN (Logic hiện tại của bạn) ---
    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);
    }

    // --- HÀM XÓA TOKEN (Dùng khi Logout) ---
    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}