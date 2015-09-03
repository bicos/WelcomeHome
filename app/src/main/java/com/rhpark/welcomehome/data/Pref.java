package com.rhpark.welcomehome.data;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rhpark.welcomehome.gson.InterfaceAdapter;

/**
 * Created by rhpark on 2015. 8. 31..
 */
public class Pref {

    public static final String PREF_HOME_LAT = "pref_home_lat";
    public static final String PREF_HOME_LON = "pref_home_lon";

    public static final String PREF_HOME_IMG_PATH = "pref_home_img_path";
    public static final String PREF_USER_INFO = "pref_user_info";

    private static SharedPreferences mPrefs;

    public static SharedPreferences getSharedPreferences() {
        return mPrefs;
    }

    public static void setupDefaults(SharedPreferences prefs) {
        mPrefs = prefs;
    }

    public static void setUser(User user) {
        Log.i("test","set user : "+user.toString());

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserContentImpl.class, new InterfaceAdapter<UserContentImpl>());
        Gson gson = builder.create();

        getSharedPreferences().edit()
//                .putString(PREF_USER_INFO, gson.toJson(user.getContents().toArray()))
                .putString(PREF_USER_INFO, gson.toJson(user))
                .apply();
    }

    public static User getUser(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserContentImpl.class, new InterfaceAdapter<UserContentImpl>());
        Gson gson = builder.create();

        String json = getSharedPreferences().getString(PREF_USER_INFO, "");

        Log.i("test","json : "+json);

        if (TextUtils.isEmpty(json) == false) {
            User user = gson.fromJson(json, User.class);

            Log.i("test", "get user : " + user.toString());
            return user;
        }
        return null;
    }
}
