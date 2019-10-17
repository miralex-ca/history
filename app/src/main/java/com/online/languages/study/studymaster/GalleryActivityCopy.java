package com.online.languages.study.studymaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.languages.study.studymaster.adapters.DataModeDialog;
import com.online.languages.study.studymaster.adapters.DividerItemDecoration;
import com.online.languages.study.studymaster.adapters.GalleryAdapter;
import com.online.languages.study.studymaster.adapters.OpenActivity;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.NavSection;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.ViewCategory;
import com.online.languages.study.studymaster.data.ViewSection;

import java.util.ArrayList;

import static com.online.languages.study.studymaster.Constants.SET_GALLERY_LAYOUT;
import static com.online.languages.study.studymaster.Constants.SET_GALLERY_LAYOUT_DEFAULT;


public class GalleryActivityCopy extends AppCompatActivity {


    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    OpenActivity openActivity;

    NavStructure navStructure;

    String tSectionID = "01010";
    String tCatID = "01010";

    ViewSection viewSection;
    NavSection navSection;

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

        openActivity = new OpenActivity(this);

        openActivity.setOrientation();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemsList = findViewById(R.id.items_list);
        cardsList = findViewById(R.id.cards_list);

        navStructure = getIntent().getParcelableExtra(Constants.EXTRA_NAV_STRUCTURE);
        tSectionID = getIntent().getStringExtra(Constants.EXTRA_SECTION_ID);
        tCatID = getIntent().getStringExtra(Constants.EXTRA_CAT_ID);

        navSection = navStructure.getNavSectionByID(tSectionID);

        setTitle(navSection.title);

        viewSection = new ViewSection(this, navSection, tCatID);

        organizeSection();

    }


    private void organizeSection() {

        ArrayList<CatSet> catSetsList = new ArrayList<>();
        CatSet catSet = new CatSet();

        for (int i=0; i < viewSection.categories.size(); i++) {
            ViewCategory viewCategory = viewSection.categories.get(i);
            viewCategory.tag = "tag"+i;

            if (viewCategory.type.equals("set")) {
                if (catSet.catList.size()>0) catSetsList.add(catSet);
                catSet = new CatSet();
                catSet.title = viewCategory.title;
            } else {
                catSet.catList.add(viewCategory);
                if (i == (viewSection.categories.size()-1)) catSetsList.add(catSet);
            }
        }

        for (CatSet group: catSetsList) {
            addList(group);
            addGrid(group);
        }

    }


    private void addList(CatSet group) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.gallery_items_list, null);

        View titleWrap = item.findViewById(R.id.titleWrap);
        TextView title = item.findViewById(R.id.title);

        if (group.title.equals("none")) {
            title.setVisibility(View.GONE);
        } else if (group.title.equals("gone")) {
            titleWrap.setVisibility(View.GONE);
        } else {
            title.setText(group.title);
            title.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = item.findViewById(R.id.recycler_view);
        GalleryAdapter mAdapter = new GalleryAdapter(this, group.catList, 1, themeTitle);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration( new DividerItemDecoration(this) );
        recyclerView.setSelected(true);
        recyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        itemsList.addView(item);

    }


    private void addGrid(CatSet group) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.gallery_grid_list, null);

        TextView title = item.findViewById(R.id.title);
        View titleWrap = item.findViewById(R.id.titleWrap);

        if (group.title.equals("none")) {
            title.setVisibility(View.GONE);
        } else if (group.title.equals("gone")) {
            titleWrap.setVisibility(View.GONE);
        } else {
            title.setText(group.title);
            title.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerViewCards = item.findViewById(R.id.recycler_view);
        GalleryAdapter cardsAdapter = new GalleryAdapter(this, group.catList, 2, themeTitle);

        recyclerViewCards.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewCards.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCards.setSelected(true);
        recyclerViewCards.setAdapter(cardsAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerViewCards, false);
        cardsList.addView(item);
    }

    private void changeLayoutStatus() {

        if (listType == 1) listType = 2;
        else listType = 1;

        SharedPreferences.Editor editor = appSettings.edit();
        editor.putInt(SET_GALLERY_LAYOUT, listType);
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

    public void openCatFromGallery(final View view) {
        ViewGroup v = (ViewGroup) view.getParent();
        View tagged = v.findViewById(R.id.tagged);
        final String tag = (String) tagged.getTag();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 openCatActivity(tag);
            }
        }, 50);
    }

    public void openCatActivity(String tag) {

        ViewCategory viewCategory = new ViewCategory();

        for (ViewCategory category: viewSection.categories) {
            if (category.tag.equals(tag)) viewCategory = category;
        }

        openActivity.openFromViewCat(navStructure, tSectionID, viewCategory);
    }

    public void pageTransition() {
        if ( !getResources().getBoolean(R.bool.wide_width)) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // updateContent();
        listType = appSettings.getInt(SET_GALLERY_LAYOUT, SET_GALLERY_LAYOUT_DEFAULT);
        applyLayoutStatus(listType);
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
        listType = appSettings.getInt(SET_GALLERY_LAYOUT, SET_GALLERY_LAYOUT_DEFAULT);
        applyLayoutStatus(listType);

        return true;
    }


    public void showInfoDialog() {
        DataModeDialog dataModeDialog = new DataModeDialog(this);
        dataModeDialog.createDialog(getString(R.string.info_txt), getString(R.string.info_gallery_txt));
    }


    private int getDrawableIcon(int iconAttr) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(iconAttr, typedValue, true);
        int drawableRes = typedValue.resourceId;
        return drawableRes;
    }

    public class CatSet {
        public ArrayList<ViewCategory> catList = new ArrayList<>();
        public String title = "none";
        private CatSet() {
        }
    }







}
