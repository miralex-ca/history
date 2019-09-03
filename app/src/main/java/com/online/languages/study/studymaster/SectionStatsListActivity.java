package com.online.languages.study.studymaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.adapters.CustomSectionAdapter;
import com.online.languages.study.studymaster.adapters.DataModeDialog;
import com.online.languages.study.studymaster.adapters.DividerItemDecoration;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.CustomCatData;
import com.online.languages.study.studymaster.data.NavSection;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.Section;
import com.online.languages.study.studymaster.data.UserStats;
import com.online.languages.study.studymaster.fragments.CatTabFragment1;

import java.util.ArrayList;

public class SectionStatsListActivity extends AppCompatActivity {

    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    private CustomSectionAdapter mAdapter;

    RecyclerView recyclerView;

    int dataType = 0;

    String list_type = Constants.LIST_STUDIED;

    int mainColor = -1;

    ArrayList<Section> sectionsCustomList;

    UserStats userStats;

    TextView dataCountTxt, dataTitle, dataDesc;


    NavStructure navStructure;
    String tSectionID = "01010";
    Section section;
    NavSection navSection;

    DBHelper dbHelper;
    int update = 0;

    Boolean full_version;

    Boolean easy_mode;
    DataModeDialog dataModeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();

        full_version = appSettings.getBoolean(Constants.SET_VERSION_TXT, false);

        easy_mode = appSettings.getString(Constants.SET_DATA_MODE, "2").equals("1");
        dataModeDialog = new DataModeDialog(this);

        setContentView(R.layout.activity_cat_data_stats);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        navStructure = getIntent().getParcelableExtra(Constants.EXTRA_NAV_STRUCTURE);
        tSectionID = getIntent().getStringExtra(Constants.EXTRA_SECTION_ID);

        section = new Section(navStructure.getNavSectionByID(tSectionID), this);

        navSection = navStructure.getNavSectionByID(tSectionID);

        dbHelper = new DBHelper(this);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.title_custom_txt);

        dataCountTxt = findViewById(R.id.sectionCustomDataCount);
        dataTitle = findViewById(R.id.customListTile);
        dataDesc = findViewById(R.id.customListDesc);

        dataType = getIntent().getIntExtra(Constants.EXTRA_DATA_TYPE, 0);


        recyclerView = findViewById(R.id.recycler_view);


        section = dbHelper.getSectionCatItemsStats(section);
        setContent();

        mAdapter = new CustomSectionAdapter(this, section.categories, mainColor, dataType);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

       // recyclerView.addItemDecoration(new DividerItemDecoration(this));


        recyclerView.setSelected(true);

        recyclerView.setAdapter(mAdapter);


        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

               // int count  = (int) view.findViewById(R.id.sectionTitle).getTag(R.id.data_count);
               // if (count  == 0) displayEmtySection();

                onListItemClick(position);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ( !getResources().getBoolean(R.bool.wide_width)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.stats_mode, menu);

        MenuItem modeMenuItem = menu.findItem(R.id.easy_mode);
        if (easy_mode) modeMenuItem.setVisible(true);

        return true;

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

            case R.id.easy_mode:
                dataModeDialog.openDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void getStatsCount() {
        UserStats userStats = new UserStats(this);

        String wCount = "";
        switch (list_type) {
            case "studied":
            //    wCount = userStats.userStatsData.studiedWords;
                break;
            case "known":
             //   wCount = userStats.userStatsData.knownWords;
                break;
            case "familiar":
            //    wCount = userStats.userStatsData.familiarWords;
                break;
            case "seen":
            //    wCount = userStats.userStatsData.seenWords;
                break;
        }

       // wordsCountTxt.setText(wCount);
    }


    private void startContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContent();
            }
        }, 30);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateContent();

    }

    public void updateContent() {
        section = dbHelper.getSectionCatItemsStats(section);
        setContent();

        mAdapter.notifyDataSetChanged();
    }


    public void setContent() {

        int dataCount = 0;

        String title = "Изучено в разделе";
        String desc = "статус «изучено»";

        if (dataType == 0) {
            dataCount = section.studiedDataCount;
        }
        if (dataType == 1) {
            dataCount = section.familiarDataCount;

            title = "Пройдено в разделе";
            desc = "статус «пройдено» и «изучено»";
        }
        if (dataType == 2) {
            dataCount = section.unknownDataCount;
            title = "Не пройдено в разделе";
            desc = "статус «не пройдено»";
        }

        dataCountTxt.setText(String.valueOf(dataCount));
        dataTitle.setText(title);
        dataDesc.setText(desc);
    }



    public void onListItemClick(int position) {

            final int act = position;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openList (act);
                }
            }, 50);

    }


    public void openCat (int position) {

        if (!full_version) {
            if (!navSection.unlocked) {
                notifyLocked();
                return;
            }
        }

        Intent intent = new Intent(this, CatActivity.class);
        intent.putExtra(Constants.EXTRA_FORCE_STATUS, "always");

        intent.putExtra(Constants.EXTRA_DATA_TYPE, dataType);

        intent.putExtra(Constants.EXTRA_CAT_SPEC, section.categories.get(position).spec);

        intent.putExtra(Constants.EXTRA_CAT_ID, section.categories.get(position).id);
        intent.putExtra("cat_title", section.categories.get(position).title);
        startActivityForResult(intent, 1);
        pageTransition();
    }



    public void openList (int position) {

        if (!full_version) {
            if (!navSection.unlocked) {
                notifyLocked();
                return;
            }
        }


        Intent intent = new Intent(this, CustomDataActivity.class);

        intent.putExtra(Constants.EXTRA_SECTION_ID, tSectionID);
        intent.putExtra(Constants.EXTRA_DATA_TYPE, dataType);
        intent.putExtra(Constants.EXTRA_CAT_ID, section.categories.get(position).id);
        intent.putExtra(Constants.EXTRA_NAV_STRUCTURE, navStructure);

        startActivityForResult(intent, 1);
        pageTransition();

    }




    public void notifyLocked() {
        Snackbar.make(recyclerView, Html.fromHtml("<font color=\"#ffffff\">Доступно в полной версии</font>"), Snackbar.LENGTH_SHORT).setAction("Action", null).show();

    }


    public void displayEmtySection() {
        Snackbar.make(recyclerView, Html.fromHtml("<font color=\"#ffffff\">Нет записей</font>"), Snackbar.LENGTH_SHORT).show();
    }


    public void pageTransition() {
        if ( !getResources().getBoolean(R.bool.wide_width)) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }


    private void colorTxt(TextView tv, String list_type) {

        int color = -1;

       // if (list_type.equals(Constants.LIST_SEEN))  color = ContextCompat.getColor(this, colors[0]);
       // if (list_type.equals(Constants.LIST_FAMILIAR)) color = ContextCompat.getColor(this, colors[1]);
       // if (list_type.equals(Constants.LIST_KNOWN)) color = ContextCompat.getColor(this, colors[2]);

        if (color != -1) {
            tv.setTextColor(color);
            mainColor = color;
        }
    }




    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){
            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }


}
