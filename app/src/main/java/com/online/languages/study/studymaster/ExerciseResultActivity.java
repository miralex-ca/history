package com.online.languages.study.studymaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DataManager;
import com.online.languages.study.studymaster.data.DetailItem;
import com.online.languages.study.studymaster.data.TestResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExerciseResultActivity extends AppCompatActivity {


    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    DBHelper dbHelper;
    ArrayList<DataItem> originList;
    TestResult testResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, 2);
        themeAdapter.getTheme();


        setContentView(R.layout.activity_result);


        originList = getIntent().getParcelableArrayListExtra("dataItems");
        dbHelper = new DBHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String tag = getIntent().getStringExtra("id");




        testResult = new TestResult(this, originList);

        TextView textView = findViewById(R.id.text);

        textView.setText(Html.fromHtml(getData()));

    }


    private String getData() {
        return testResult.prepareDisplay();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(0, R.anim.fade_out_2);
    }



}
