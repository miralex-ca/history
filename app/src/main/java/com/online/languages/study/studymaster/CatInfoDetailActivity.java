package com.online.languages.study.studymaster;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.DataFromJson;
import com.online.languages.study.studymaster.data.DetailFromJson;
import com.online.languages.study.studymaster.data.DetailItem;
import com.squareup.picasso.Picasso;


public class CatInfoDetailActivity extends AppCompatActivity {

    LinearLayout box;
    View mask;
    DetailItem detailItem;

    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_info_dialog1);



        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }




        //TextView titleT = (TextView) findViewById(R.id.text);
        TextView infoT = (TextView) findViewById(R.id.lbl_text);
        TextView imgIfo = (TextView) findViewById(R.id.lbl_img_desc);

        box = (LinearLayout) findViewById(R.id.box);
        mask = (View) findViewById(R.id.mask);

        //box.setAlpha(0.0f);
       // box.setVisibility(View.VISIBLE);
        //box.animate().alpha(1.0f).start();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);



        String tag = getIntent().getStringExtra("id");

        detailItem = new DetailFromJson(this, tag).detail;

        collapsingToolbar.setTitle(detailItem.title);

        infoT.setText(detailItem.desc);
        imgIfo.setText("Изображение: "+detailItem.img_info);


        //Toast.makeText(this, detailItem.image , Toast.LENGTH_SHORT).show();

        ImageView placePicutre = (ImageView) findViewById(R.id.image);

        //*/
        Picasso.with(this)
                //.load(R.drawable.f)
                //.load(R.raw.e)
                .load("file:///android_asset/pics/"+detailItem.image)
                .fit()
                .centerCrop()
                .into(placePicutre);

        //*/

        if ( getResources().getBoolean(R.bool.tablet) || (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) ) {
            introAnimation();
        }
    }


    private void introAnimation() {
        box.setY(300.0f);
        box.setAlpha(0.0f);
        box.animate().
                translationY(0.0f)
                .setDuration(500)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        box.animate().alpha(1.0f).setDuration(300).start();

        mask.setAlpha(0.0f);
        mask.animate()
                .alpha(1.0f)
                .setDuration(500)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void exitAnimation() {
        box.animate().
                translationY(1300.0f)
                .setDuration(250)
                .setInterpolator(new FastOutLinearInInterpolator())
                .start();

        box.animate().alpha(0.0f)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .start();

        mask.animate()
                .alpha(0.0f)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                        overridePendingTransition(0,0);
                    }
                })
                .start();
    }


    public void dialogClose(View view) {
        //supportFinishAfterTransition();
        exitAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                exitAnimation();
                return true;
            case R.id.exit_from_menu:
                exitAnimation();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitAnimation();
    }
}
