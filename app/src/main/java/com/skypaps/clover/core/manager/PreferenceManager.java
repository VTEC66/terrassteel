package com.skypaps.clover.core.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;

public class PreferenceManager {

    Context mContext;

    Gson gson;

    enum Keys {

        KEY_COOKIES("KEY_COOKIES"),
        KEY_SOULD_SHOW_MARKET_SCREENS("KEY_SOULD_SHOW_MARKET_SCREENS"),
        KEY_EMAIL("KEY_EMAIL"),
        KEY_ACCESS_TOKEN("KEY_ACCESS_TOKEN");
        private String key;

        Keys(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    }

    private Set<Cookie> cookies = new HashSet<>();


    public PreferenceManager(Context context, Gson gson) {
        this.mContext = context;
        this.gson = gson;
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE);
    }

    public synchronized Set<Cookie> getCookies() {
        if (cookies.isEmpty() && gson != null) {
            String cookies = getDefaultSharedPreferences(mContext).getString(Keys.KEY_COOKIES.getKey(), "");
            Type type = new TypeToken<Set<Cookie>>() {
            }.getType();
            this.cookies = gson.fromJson(cookies, type);
            if (this.cookies == null) {
                this.cookies = new HashSet<>(0);
            }
        }
        return Collections.unmodifiableSet(cookies);
    }

    public void clearCookies() {
        cookies.clear();
        getDefaultSharedPreferences(mContext).edit().putString(Keys.KEY_COOKIES.getKey(), null).apply();
    }


    public boolean shouldShowMarketScreen() {
        return getDefaultSharedPreferences(mContext).getBoolean(Keys.KEY_SOULD_SHOW_MARKET_SCREENS.getKey(), true);
    }

    public void setShowMarketScreen(boolean showMarketScreen) {
        getDefaultSharedPreferences(mContext).edit().putBoolean(Keys.KEY_SOULD_SHOW_MARKET_SCREENS.getKey(), showMarketScreen).apply();
    }


    public void saveEmail(String email) {
        getDefaultSharedPreferences(mContext).edit().putString(Keys.KEY_EMAIL.getKey(), email).apply();
    }

    public String getEmail() {
        return getDefaultSharedPreferences(mContext).getString(Keys.KEY_EMAIL.getKey(), "");
    }


    public boolean hasEmailStored() {
        return !getDefaultSharedPreferences(mContext).getString(Keys.KEY_EMAIL.getKey(), "").isEmpty();
    }


    public void saveAccessToken(String accessToken) {
        getDefaultSharedPreferences(mContext).edit().putString(Keys.KEY_ACCESS_TOKEN.getKey(), accessToken).apply();
    }

    public String getAccessToken() {
        return getDefaultSharedPreferences(mContext).getString(Keys.KEY_ACCESS_TOKEN.getKey(), "");
    }

    public void clear(){
        for (Keys key : Keys.values()) {
            getDefaultSharedPreferences(mContext).edit().remove(key.getKey()).apply();
        }
    }
}
