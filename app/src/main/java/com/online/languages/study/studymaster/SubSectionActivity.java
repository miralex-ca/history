package com.online.languages.study.studymaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.online.languages.study.studymaster.adapters.CatsListAdapter;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.NavSection;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.ViewCategory;
import com.online.languages.study.studymaster.data.ViewSection;


public class SubSectionActivity extends BaseActivity {

    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    RecyclerView recyclerView;
    CatsListAdapter mAdapter;

    NavStructure navStructure;

    String tSectionID = "01010";
    String tCatID = "01010";

    ViewSection viewSection;

    NavSection navSection;

    Boolean full_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_section);

        full_version = appSettings.getBoolean(Constants.SET_VERSION_TXT, false);


        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.subsection_title);

        navStructure = getIntent().getParcelableExtra(Constants.EXTRA_NAV_STRUCTURE);
        tSectionID = getIntent().getStringExtra(Constants.EXTRA_SECTION_ID);
        tCatID = getIntent().getStringExtra(Constants.EXTRA_CAT_ID);

        navSection = navStructure.getNavSectionByID(tSectionID);
        viewSection = new ViewSection(this, navSection, tCatID);
        viewSection.getProgress();

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new CatsListAdapter(this, viewSection.categories, full_version);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setSelected(true);
        recyclerView.setAdapter(mAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    private void updateContent() {
        viewSection.getProgress();
        mAdapter = new CatsListAdapter(this, viewSection.categories, full_version);
        recyclerView.setAdapter(mAdapter);
    }

    public void openCat(final View view) {

        final int position = (int) view.getTag();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openCatActivity(position);
            }
        }, 50);
    }

    public void openCatActivity(int position) {

        ViewCategory viewCategory = viewSection.categories.get(position);

        if (viewCategory.type.equals("set")) return;

        Intent i;

        if (viewCategory.type.equals("group")) {

            i = new Intent(SubSectionActivity.this, SubSectionActivity.class);

            i.putExtra(Constants.EXTRA_CAT_ID, viewCategory.id);
            i.putExtra(Constants.EXTRA_SECTION_ID, tSectionID);
            i.putExtra(Constants.EXTRA_NAV_STRUCTURE, navStructure);

        } else {


            if (viewCategory.spec.equals("map")) {
                i = new Intent(SubSectionActivity.this, MapActivity.class);
                i.putExtra("page_id", viewCategory.id);

            } else {
                i = new Intent(SubSectionActivity.this, CatActivity.class);
                i.putExtra(Constants.EXTRA_CAT_ID, viewCategory.id);
                i.putExtra(Constants.EXTRA_CAT_SPEC, viewCategory.spec);
                i.putExtra("cat_title", viewCategory.title);
            }
        }

        // i.putExtra("show_ad", showAd);

        startActivityForResult(i, 1);
        pageTransition();
    }

    public void pageTransition() {
        if ( !getResources().getBoolean(R.bool.wide_width)) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateContent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ( !getResources().getBoolean(R.bool.wide_width)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                if ( !getResources().getBoolean(R.bool.wide_width)) {
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }





}
