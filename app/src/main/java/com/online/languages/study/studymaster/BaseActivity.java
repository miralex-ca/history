package com.online.languages.study.studymaster;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    BroadcastReceiver br;

    @Override
    protected void onDestroy() {

        unregisterReceiver(br);

        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        br = new LocaleChangedReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        registerReceiver(br, filter);

    }
}