package com.online.languages.study.studymaster;


import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.adapters.ColorProgress;
import com.online.languages.study.studymaster.adapters.ResizeHeight;
import com.online.languages.study.studymaster.adapters.TestResultAdapter;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.TestResult;

import java.util.ArrayList;

import static com.online.languages.study.studymaster.adapters.ViewAnimatorSlideUpDown.slideDown;
import static com.online.languages.study.studymaster.adapters.ViewAnimatorSlideUpDown.slideUp;

public class ExerciseResultActivity extends AppCompatActivity {

    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    DBHelper dbHelper;
    ArrayList<DataItem> originList;
    TestResult testResult;

    RecyclerView recyclerView;
    TestResultAdapter mAdapter;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Результат теста");

        testResult = new TestResult(this, originList);

        ArrayList<TestResult.ResultCategory> cats;

        if (testResult.errorSections.size() > 2 ) {
            cats = testResult.errorSections;
        } else {
            cats = testResult.errorCategories;
        }

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new TestResultAdapter(this, cats);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setSelected(true);
        recyclerView.setAdapter(mAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        TextView taskCount = findViewById(R.id.tasksTotalCount);
        TextView errorCount = findViewById(R.id.errorsTotalCount);
        TextView skippedCount = findViewById(R.id.skippedTotalCount);
        TextView testProgress = findViewById(R.id.testProgress);

        taskCount.setText("Всего заданий: " + originList.size());
        errorCount.setText("Ошибок: " + testResult.testErrors.size());
        skippedCount.setText("Пропущено: " + testResult.unanswered.size());

        int correct =  originList.size() - testResult.testErrors.size() - testResult.unanswered.size();

        double res = ((double) correct ) / originList.size() * 100;

        testProgress.setText((int)res +"%");
        ColorProgress colorProgress = new ColorProgress(this);
        testProgress.setTextColor(  colorProgress.getColorFromAttr( (int)res)  );



    }





    private String getData() {
        return testResult.prepareDisplay();
    }


    public void openView(View view) {

        View r = (View) ((ViewGroup) view.getParent()).getParent();

        final View v = r.findViewById(R.id.sectionContent);
        final ImageView icon = r.findViewById(R.id.expIcon);
        final TextView title = r.findViewById(R.id.sectionTitle);

        String status = (String) v.getTag(R.integer.view_tag_1);

        if (status.equals("closed")) {
            //slideDown(v);

            v.setTag(R.integer.view_tag_1, "open");

            int h = (int) v.getTag(R.integer.view_tag_2);

            icon.setImageResource(R.drawable.ic_chevron_up);

            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.height = 0;
            v.setLayoutParams(layoutParams);

            v.setVisibility(View.VISIBLE);

            ResizeHeight resizeHeight = new ResizeHeight(v, h, 0);
            resizeHeight.setDuration(170);
            v.startAnimation(resizeHeight);

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    v.animate().alpha(1.0f);
                }
            }, 200);
        }

        if (status.equals("open")) {

            v.animate().alpha(0);

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {

                    icon.setImageResource(R.drawable.ic_chevron_down);
                    slideUp(v);
                    v.setTag(R.integer.view_tag_1,"closed");
                }
            }, 200);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(0, R.anim.fade_out_2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fade_out_2);
    }



}
