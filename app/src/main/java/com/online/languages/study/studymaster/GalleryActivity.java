package com.online.languages.study.studymaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.online.languages.study.studymaster.adapters.DataModeDialog;
import com.online.languages.study.studymaster.adapters.OpenActivity;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.NavSection;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.ViewCategory;
import com.online.languages.study.studymaster.data.ViewSection;
import com.online.languages.study.studymaster.fragments.GalleryFragment;

import static com.online.languages.study.studymaster.Constants.EXTRA_CAT_ID;
import static com.online.languages.study.studymaster.Constants.EXTRA_SECTION_ID;
import static com.online.languages.study.studymaster.Constants.GALLERY_REQUESTCODE;


public class GalleryActivity extends AppCompatActivity {


    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    OpenActivity openActivity;

    NavStructure navStructure;

    String tSectionID = "01010";
    String tCatID = "01010";

    ViewSection viewSection;
    NavSection navSection;

    GalleryFragment galleryFragment;

    LinearLayout itemsList, cardsList;

    private MenuItem changeLayoutBtn;

    int listType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        openActivity = new OpenActivity(this);

        navStructure = getIntent().getParcelableExtra(Constants.EXTRA_NAV_STRUCTURE);
        tSectionID = getIntent().getStringExtra(EXTRA_SECTION_ID);
        tCatID = getIntent().getStringExtra(EXTRA_CAT_ID);


        galleryFragment = new GalleryFragment();


        Bundle bundle = new Bundle();
        bundle.putParcelable("structure", navStructure);
        bundle.putString(EXTRA_SECTION_ID, tSectionID);
        bundle.putString(EXTRA_CAT_ID, tCatID);

        galleryFragment.setArguments(bundle);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, galleryFragment).commit();


    }


    public void openCatFromGallery(final View view) {
        ViewGroup v = (ViewGroup) view.getParent();
        View tagged = v.findViewById(R.id.tagged);
        final String tag = (String) tagged.getTag();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (galleryFragment!=null) galleryFragment.openCatActivity(tag);
            }
        }, 50);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // updateContent();

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUESTCODE) {
            if (galleryFragment != null) {
                galleryFragment.onActivityResult(requestCode, resultCode, data);
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivity.pageBackTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                openActivity.pageBackTransition();
                return true;
            case R.id.info_from_menu:
                showInfoDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery_activity, menu);
        return true;
    }


    public void showInfoDialog() {
        DataModeDialog dataModeDialog = new DataModeDialog(this);
        dataModeDialog.createDialog(getString(R.string.info_txt), getString(R.string.info_gallery_txt));
    }



}
