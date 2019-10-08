package com.online.languages.study.studymaster;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.adapters.TouchImageView;
import com.online.languages.study.studymaster.data.Category;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DataManager;
import com.online.languages.study.studymaster.data.DetailItem;
import com.online.languages.study.studymaster.data.ImageData;
import com.online.languages.study.studymaster.data.ImageMapsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;


    ImageData mapData;
    ImageMapsData imageMapsData;

    int picType = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        if (themeTitle.equals("white")) themeTitle = "white_map";

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();


        super.onCreate(savedInstanceState);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }


        setContentView(R.layout.activity_map);


        if(getResources().getBoolean(R.bool.portrait_only)){
           // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageMapsData = new ImageMapsData(this);


        String mapId = getIntent().getStringExtra("page_id");

        picType = getIntent().getIntExtra("pic", 0);


        String folder = "maps/";
        if (picType != 1) {
            mapData = imageMapsData.getMapInfoById(mapId);
        } else {
            folder = "pics/";
            mapData = getDataFromDetail(mapId);
        }


        float maxZoomRatio = 2.4f;
        if (picType == 1) maxZoomRatio = 1.5f;


        setTitle(mapData.title);

        TouchImageView imageView = findViewById(R.id.frag_imageview);
        imageView.setMaxZoomRatio(maxZoomRatio);


        Picasso.with( this )
                //.load(R.drawable.f)
                //.load(R.raw.e)
                .load("file:///android_asset/"+folder+ mapData.image)
                .into(imageView);


    }


    private ImageData getDataFromDetail(String id) {

        DetailItem detailItem;

        DataManager dataManager = new DataManager(this);

        detailItem = dataManager.getDetailFromDB(id);

        if (detailItem.title.equals("not found")) {
            DataItem dataItem =  dataManager.getDataItemFromDB(id);
            dataItem.item = dataItem.item + "\n\n" + dataItem.info;
            detailItem  = new DetailItem(dataItem);
        }

        return new ImageData(detailItem.title, "", detailItem.id, detailItem.image);
    }



    private void pageTransition() {
        if (picType == 1) {
            overridePendingTransition(R.anim.fade_in_img_back, R.anim.fade_out_img_back);

        } else {

            if ( !getResources().getBoolean(R.bool.wide_width)) {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pageTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                pageTransition();

                return true;

            case R.id.pic_info:
                showInfoDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_info, menu);
        return true;
    }


    public void showInfoDialog() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.dialog_pic_info, null);


        TextView title = content.findViewById(R.id.picTitle);
        TextView info = content.findViewById(R.id.picInfo);

        View webLink = content.findViewById(R.id.webLink);

        if (mapData.weblink.equals("")) webLink.setVisibility(View.GONE);

        title.setText("Изображение: " + mapData.title);

        info.setText(mapData.desc);

        if (mapData.desc.equals("")) info.setVisibility(View.GONE);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сведения об изображении")
                .setCancelable(true)
                .setNegativeButton(R.string.dialog_close_txt,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })

                ///.setMessage(Html.fromHtml(resultTxt))

                .setView(content);

        AlertDialog alert = builder.create();
        alert.show();

    }


    public void openWebLink(View view) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(mapData.weblink));
        try {
            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MapActivity.this, R.string.msg_no_browser, Toast.LENGTH_SHORT).show();
        }

    }






}
