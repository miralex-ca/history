package com.online.languages.study.studymaster.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.online.languages.study.studymaster.BuildConfig;
import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.data.DataManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class InfoFragment extends Fragment {

    SharedPreferences appSettings;
    public String themeTitle;

    View button;


    String htmlStart = "<!DOCTYPE html><html><head><style>";



    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_info, container, false);

        WebView webView = rootview.findViewById(R.id.webView);

        DataManager dataManager  = new DataManager(getActivity(), true);

        String resName = "info";
        if (dataManager.simplified) resName = "info_simplified";

        Context context = getActivity().getBaseContext(); //получаем контекст

        String text = readRawTextFile(context, getResources().getIdentifier(resName, "raw", getActivity().getPackageName()));

        String info = htmlStart + getThemeFont() + text;

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        webView.loadDataWithBaseURL(null, info, "text/html", "en_US", null);
        webView.setBackgroundColor(Color.TRANSPARENT);

        button = rootview.findViewById(R.id.refButton);
        button.setAlpha(0.0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.animate().alpha(1f).setDuration(300);
            }
        }, 700);

        TextView versionName = rootview.findViewById(R.id.versionName);
        versionName.setText(versionText());



        return rootview;

    }

    public static String readRawTextFile(Context context, int resId)
    {
        InputStream inputStream = context.getResources().openRawResource(resId);

        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputReader);
        String line;
        StringBuilder builder = new StringBuilder();

        try {
            while (( line = buffReader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            return null;
        }
        return builder.toString();
    }

    private String getThemeFont () {

        appSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        String color = "body {color: #111;}";

        if (themeTitle.contains("dark") || themeTitle.contains("smoky")|| themeTitle.contains("westworld")) {
            color= "body {color: #fff;}";
        }

        return color;

    }

    private String versionText() {

        String str = getString(R.string.app_name);

        String spec = getString(R.string.app_name_spec);

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        str = str + " (" + spec + versionName + ")";

        return str;

    }



}
