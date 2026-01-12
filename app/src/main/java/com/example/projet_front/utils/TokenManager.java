package com.example.projet_front.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_COUNTRY = "user_country";
    private static final String KEY_USER_LANGUAGE = "user_language";
    private static final String KEY_USER_CURRENCY = "user_currency";

    private final SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUserId(int userId) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public void saveUserName(String name) {
        prefs.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public void saveUserEmail(String email) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public void saveUserCountry(String country) {
        prefs.edit().putString(KEY_USER_COUNTRY, country).apply();
    }

    public String getUserCountry() {
        return prefs.getString(KEY_USER_COUNTRY, "");
    }

    public void saveUserLanguage(String language) {
        prefs.edit().putString(KEY_USER_LANGUAGE, language).apply();
    }

    public String getUserLanguage() {
        return prefs.getString(KEY_USER_LANGUAGE, "");
    }

    public void saveUserCurrency(String currency) {
        prefs.edit().putString(KEY_USER_CURRENCY, currency).apply();
    }

    public String getUserCurrency() {
        return prefs.getString(KEY_USER_CURRENCY, "");
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }
}