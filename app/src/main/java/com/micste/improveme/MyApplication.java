package com.micste.improveme;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by micste on 2017-09-14.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
