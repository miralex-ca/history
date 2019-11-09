package com.online.languages.study.studymaster.adapters;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.online.languages.study.studymaster.LocaleChangedReceiver;
import com.online.languages.study.studymaster.R;

import static com.online.languages.study.studymaster.App.getAppContext;


public class ThemeAdapter {

    private Context context;


    public int styleTheme;

    private int colorListTxt;
    private Boolean transparentStatus = false;

    private Boolean dialog = false;

    public ThemeAdapter() {

    }

    public ThemeAdapter(Context _context, String _theme, int layout) {

        setTheme(_context, _theme, false, true);
    }


    public ThemeAdapter(Context _context, String _theme, Boolean transparent) {

        setTheme(_context, _theme, transparent, false);

    }

    private void setTheme(Context _context, String _theme, Boolean transparent, Boolean _dialog) {

        context = _context;
        transparentStatus = transparent;

        dialog = _dialog;

        styleTheme = getThemeStyleName(_theme);
        colorListTxt = getColorFromAttr(R.attr.colorListTxt);


       // Toast.makeText(context, "Dialog: "+ dialog, Toast.LENGTH_SHORT).show();




    }


    private int getThemeStyleName(String theme) {
        int themeStyle = R.style.DefaultTheme;

        switch (theme) {
            case "default":
                themeStyle =  R.style.DefaultTheme;
                if (transparentStatus) themeStyle =  R.style.DefaultTheme;
                if (dialog) themeStyle =  R.style.DefaultTheme_UserDialog;
                break;

            case "red":
                    themeStyle =  R.style.RedTheme;
                    if (transparentStatus) themeStyle =  R.style.RedTheme;
                    if (dialog) themeStyle =  R.style.RedTheme_Detail;
                    break;
            case "white":
                themeStyle =  R.style.WhiteTheme;
                if (transparentStatus) themeStyle = R.style.WhiteTheme;
                if (dialog) themeStyle =  R.style.WhiteTheme_Detail;
                break;

            case "white_map":
                themeStyle =  R.style.WhiteThemeMap;
                break;

            case "dark":
                themeStyle = R.style.DarkTheme;
                if (transparentStatus) themeStyle = R.style.DarkTheme_TransparentStatus;
                if (dialog) themeStyle =  R.style.DarkTheme_UserDialog;
                break;
            case "smoky":
                themeStyle = R.style.SmokyTheme;
                if (transparentStatus) themeStyle = R.style.SmokyTheme_TransparentStatus;
                if (dialog) themeStyle =  R.style.SmokyTheme_UserDialog;
                break;
            case "westworld":
                themeStyle = R.style.WestworldTheme;
                if (transparentStatus) themeStyle = R.style.WestworldTheme_TransparentStatus;
                if (dialog) themeStyle =  R.style.WestworldTheme_UserDialog;
                break;
        }

        return themeStyle;
    }


    public void getTheme() {

        context.setTheme(styleTheme);








       // BroadcastReceiver br = new LocaleChangedReceiver();

        //IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
       // filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
       // context.registerReceiver(br, filter);

       // IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
       // context.registerReceiver(br, filter);





        /*
        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //RESTART APPLICATION


                if (intent.getAction (). compareTo (Intent.ACTION_LOCALE_CHANGED) == 0)
                {


                }


                Toast.makeText(getAppContext(), "Locale changed", Toast.LENGTH_SHORT).show();
            }
        };

            //register broadcastreceiver
        LocalBroadcastManager.getInstance(context)
                .registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_LOCALE_CHANGED));



         */

        //Toast.makeText(context, "Theme: "+styleTheme, Toast.LENGTH_SHORT).show();
    }

    public int getColorFromAttr(int attr) {

        TypedArray a = context.getTheme().obtainStyledAttributes(styleTheme, new int[] {attr});

        int color = a.getResourceId(0, 0);
        a.recycle();
        return color;
    }



}
