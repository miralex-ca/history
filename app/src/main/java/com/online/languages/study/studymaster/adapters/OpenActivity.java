package com.online.languages.study.studymaster.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.data.NavStructure;

public class OpenActivity  {

    Context context;


    public OpenActivity(Context _context) {

        context = _context;

    }



    public void openCat(Intent intent, String cat_id, String title) {
        intent = catIntent(intent, cat_id, title);
        callActivity(intent);
    }

    private Intent catIntent(Intent intent, String cat_id, String title) {
        intent.putExtra(Constants.EXTRA_CAT_ID, cat_id);
        intent.putExtra("cat_title", title);
        return intent;
    }


    private void callActivity(Intent intent) {
        ((Activity) context).startActivityForResult(intent, 1);
        pageTransition();

    }


    public void pageTransition() {
        if ( !  context.getApplicationContext().getResources().getBoolean(R.bool.wide_width)) {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }


    public void openSection(Intent intent, NavStructure navStructure, String section_id, String parent) {
        intent = sectionIntent(intent, navStructure, section_id, parent);
        callActivity(intent);
    }


    private Intent sectionIntent(Intent intent, NavStructure navStructure, String section_id, String parent) {

        intent.putExtra(Constants.EXTRA_SECTION_PARENT, parent);
        intent.putExtra(Constants.EXTRA_SECTION_ID, section_id);
        intent.putExtra(Constants.EXTRA_NAV_STRUCTURE, navStructure);

        return intent;
    }











    }
