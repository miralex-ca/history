package com.online.languages.study.studymaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.online.languages.study.studymaster.adapters.ContentAdapter;
import com.online.languages.study.studymaster.adapters.DataModeDialog;
import com.online.languages.study.studymaster.adapters.DividerItemDecoration;
import com.online.languages.study.studymaster.adapters.SectionListAdapter;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DataManager;
import com.online.languages.study.studymaster.data.NavSection;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.Section;
import com.online.languages.study.studymaster.fragments.CatTabFragment1;

import java.util.ArrayList;

import static com.online.languages.study.studymaster.Constants.APP_SIMPLIFIED;

public class SectionListActivity extends AppCompatActivity {


    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    ArrayList<DataItem> data = new ArrayList<>();
    SectionListAdapter adapter;
    DataManager dataManager;

    NavSection navSection;

    Boolean easy_mode;
    DataModeDialog dataModeDialog;

    boolean fullVersion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();

        NavStructure navStructure;


        String tSectionID = "01010";


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_list);

        easy_mode = appSettings.getString(Constants.SET_DATA_MODE, "2").equals("1");
        dataModeDialog = new DataModeDialog(this);

        fullVersion = appSettings.getBoolean(Constants.SET_VERSION_TXT, false);


        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Обзор раздела");


        navStructure = getIntent().getParcelableExtra(Constants.EXTRA_NAV_STRUCTURE);
        tSectionID = getIntent().getStringExtra(Constants.EXTRA_SECTION_ID);

        navSection = navStructure.getNavSectionByID(tSectionID);

        int showStatus = Integer.valueOf(appSettings.getString("show_status", Constants.STATUS_SHOW_DEFAULT));

        dataManager = new DataManager(this);

        DBHelper dbHelper = new DBHelper(this);
        data = dbHelper.getSectionListDataItems( navSection.uniqueCategories );

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new SectionListAdapter(data, showStatus);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration( new DividerItemDecoration(this) );
        recyclerView.setAdapter(adapter);

        openView(recyclerView);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                View animObj = view.findViewById(R.id.animObj);

                String type = (String) animObj.getTag(R.id.item_type);

                if (! type.equals("group_title")) onItemClick(animObj, position);

            }
            @Override
            public void onLongClick(View view, int position) {
                if (fullVersion || navSection.unlocked) changeStarred(position);
            }
        }));

    }


    public void changeStarred(int position){   /// check just one item

        String id = data.get(position).id;
        Boolean starred = dataManager.checkStarStatusById(id );

        int status = dataManager.dbHelper.setStarred(id, !starred); // id to id


        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibLen = 30;

        if (status == 0) {
            Toast.makeText(this, R.string.starred_limit, Toast.LENGTH_SHORT).show();
            vibLen = 300;
        }

        checkStarred(position);


        assert v != null;
        v.vibrate(vibLen);
    }



    private void openView(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                view.setVisibility(View.VISIBLE);
            }
        }, 80);
    }


    private void onItemClick(final View view, final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 showAlertDialog(view, position);
            }
        }, 50);
    }



    public void showAlertDialog(View view, int position) {

        Intent intent = new Intent(SectionListActivity.this, ScrollingActivity.class);


        intent.putExtra("starrable", navSection.unlocked);

        String id = view.getTag(R.id.item_id).toString();
        intent.putExtra("id", id );
        intent.putExtra("position", position);



        intent.putExtra("item", data.get(position));

        startActivityForResult(intent,1);

        overridePendingTransition(R.anim.slide_in_down, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == CatActivity.RESULT_OK){

                int result=data.getIntExtra("result", -1);

                 checkStarred(result);
            }
        }
    }


    public void checkStarred(final int result){   /// check just one item
        data = dataManager.checkDataItemsData(data);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(result);
            }
        }, 200);
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
        getMenuInflater().inflate(R.menu.stats_mode_info, menu);
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
            case R.id.info_from_menu:
                infoMessage();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void infoMessage() {
        if (APP_SIMPLIFIED) dataModeDialog.createDialog(getString(R.string.info_txt), getString(R.string.info_star_txt));
        else dataModeDialog.modeInfoDialog();
    }


    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
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
