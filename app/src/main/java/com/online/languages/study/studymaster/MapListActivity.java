package com.online.languages.study.studymaster;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.adapters.DataModeDialog;
import com.online.languages.study.studymaster.adapters.DividerItemDecoration;
import com.online.languages.study.studymaster.adapters.MapListAdapter;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.ImageData;
import com.online.languages.study.studymaster.data.ImageMapsData;
import com.online.languages.study.studymaster.data.NavSection;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.ViewCategory;
import com.online.languages.study.studymaster.data.ViewSection;


public class MapListActivity extends AppCompatActivity {


    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    RecyclerView recyclerView;
    RecyclerView recyclerViewCards;
    MapListAdapter mAdapter;
    MapListAdapter cardsAdapter;

    NavStructure navStructure;

    String tSectionID = "01010";
    String tCatID = "01010";

    ViewSection viewSection;

    NavSection navSection;

    Boolean full_version;

    ImageMapsData imageMapsData;

    View itemsList, cardsList;

    private MenuItem changeLayoutBtn;

    int listType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);


        full_version = appSettings.getBoolean(Constants.SET_VERSION_TXT, false);

        listType = appSettings.getInt("maps_list_layout", 1);


        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Карты");

        itemsList = findViewById(R.id.items_list);
        cardsList = findViewById(R.id.cards_list);


        imageMapsData = new ImageMapsData(this);

        navStructure = getIntent().getParcelableExtra(Constants.EXTRA_NAV_STRUCTURE);
        tSectionID = getIntent().getStringExtra(Constants.EXTRA_SECTION_ID);
        tCatID = getIntent().getStringExtra(Constants.EXTRA_CAT_ID);

        navSection = navStructure.getNavSectionByID(tSectionID);
        viewSection = new ViewSection(this, navSection, navStructure.getNavCatListFromParent(tCatID, tSectionID));

        getImages();

        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new MapListAdapter(this, viewSection.categories, 1, themeTitle);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration( new DividerItemDecoration(this) );
        recyclerView.setSelected(true);
        recyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);


        recyclerViewCards = findViewById(R.id.recycler_view_cards);
        cardsAdapter = new MapListAdapter(this, viewSection.categories, 2, themeTitle);

        recyclerViewCards.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewCards.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration( new DividerItemDecoration(this) );

        recyclerViewCards.setSelected(true);
        recyclerViewCards.setAdapter(cardsAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerViewCards, false);


    }

    public void getImages() {
        for (ViewCategory cat: viewSection.categories) {
            ImageData image = imageMapsData.getMapInfoById(cat.id);
            cat.image  = image.image;
        }
    }





    private void changeLayoutStatus() {

        if (listType == 1) {
            listType = 2;
        } else {
            listType = 1;
        }

        SharedPreferences.Editor editor = appSettings.edit();
        editor.putInt("maps_list_layout", listType);
        editor.apply();

        applyLayoutStatus(listType);
    }

    private void applyLayoutStatus(int type) {

        if (type == 2) {
            cardsList.setVisibility(View.VISIBLE);
            itemsList.setVisibility(View.GONE);
            changeLayoutBtn.setIcon(getDrawableIcon(R.attr.iconList));
        } else {
            cardsList.setVisibility(View.GONE);
            itemsList.setVisibility(View.VISIBLE);
            changeLayoutBtn.setIcon(getDrawableIcon(R.attr.iconGrid2));
        }

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

            i = new Intent(MapListActivity.this, SubSectionActivity.class);

            i.putExtra(Constants.EXTRA_CAT_ID, viewCategory.id);
            i.putExtra(Constants.EXTRA_SECTION_ID, tSectionID);
            i.putExtra(Constants.EXTRA_NAV_STRUCTURE, navStructure);

        } else {


            if (viewCategory.spec.equals("map")) {
                i = new Intent(MapListActivity.this, MapActivity.class);
                i.putExtra("page_id", viewCategory.id);

            } else {
                i = new Intent(MapListActivity.this, CatActivity.class);
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
        // updateContent();
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

            case R.id.list_layout:
                changeLayoutStatus();
                return true;

            case R.id.info_from_menu:
                showInfoDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps_list, menu);
        changeLayoutBtn =  menu.findItem(R.id.list_layout);
        applyLayoutStatus(listType);
        return true;
    }


    public void showInfoDialog() {
        String message = getString(R.string.maps_info);
        if (navSection.spec.equals("gallery")) message = getString(R.string.info_menu_map);
        DataModeDialog dataModeDialog = new DataModeDialog(this);
        dataModeDialog.createDialog(getString(R.string.info_txt), message);
    }


    private int getDrawableIcon(int iconAttr) {

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(iconAttr, typedValue, true);
        int drawableRes = typedValue.resourceId;

        return drawableRes;
    }

}
