package com.example.md_08_ungdungfivestore.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.md_08_ungdungfivestore.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class FavoriteManager {

    private static final String PREFS_NAME = "favorites_prefs";
    private static final String KEY_FAVORITES = "favorites";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public FavoriteManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public Set<String> getFavoriteIds() {
        return sharedPreferences.getStringSet(KEY_FAVORITES, new HashSet<>());
    }

    public boolean isFavorite(String productId) {
        return getFavoriteIds().contains(productId);
    }

    public void addFavorite(Product product) {
        Set<String> ids = new HashSet<>(getFavoriteIds());
        ids.add(product.getId());
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, ids).apply();
    }

    public void removeFavorite(Product product) {
        Set<String> ids = new HashSet<>(getFavoriteIds());
        ids.remove(product.getId());
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, ids).apply();
    }
}
