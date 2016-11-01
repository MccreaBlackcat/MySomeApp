package com.timestudio.mynews;

import android.app.Application;
import android.content.SharedPreferences;

import com.timestudio.mynews.util.UserManager;

/**
 * Created by hasee on 2016/11/1.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = getSharedPreferences("login", 0);
        String user = preferences.getString("user", "");
        String password = preferences.getString("password", "");
        if (!user.equals("")) {
            UserManager.UserLogin(this,user,password);
        }

    }
}
