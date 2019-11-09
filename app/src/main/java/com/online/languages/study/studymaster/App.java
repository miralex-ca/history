package com.online.languages.study.studymaster;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class App extends Application {

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();

       // BroadcastReceiver br = new LocaleChangedReceiver();
       // IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
       // context.registerReceiver(br, filter);

    }




    public static Context getAppContext() {
        return App.context;
    }








}
