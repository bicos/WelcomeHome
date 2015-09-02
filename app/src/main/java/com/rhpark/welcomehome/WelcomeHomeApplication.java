package com.rhpark.welcomehome;

import android.app.Application;
import android.preference.PreferenceManager;

import com.rhpark.welcomehome.data.Pref;

/**
 * Created by rhpark on 2015. 8. 31..
 */
public class WelcomeHomeApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // setup default preference
        Pref.setupDefaults(PreferenceManager.getDefaultSharedPreferences(this));
    }
}
