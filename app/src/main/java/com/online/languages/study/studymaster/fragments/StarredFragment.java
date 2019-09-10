package com.online.languages.study.studymaster.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.adapters.ContentAdapter;
import com.online.languages.study.studymaster.adapters.DividerItemDecoration;
import com.online.languages.study.studymaster.adapters.ExRecycleAdapter;
import com.online.languages.study.studymaster.adapters.StarredAdapter;
import com.online.languages.study.studymaster.data.DataFromJson;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StarredFragment extends Fragment {


    DataManager dataManager;
    TextView text;
    TextView starredCount;
    View zero;
    View infoBox;
    TextView countZero, desc;

    StarredAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<DataItem> words;

    LinearLayout previewList;


    public StarredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_starred, container, false);



        text = rootView.findViewById(R.id.starredWords);
        dataManager = new DataManager(getActivity());
        starredCount = rootView.findViewById(R.id.starred_count);

        zero = rootView.findViewById(R.id.starred_zero);
        countZero = rootView.findViewById(R.id.starred_count_preview);
        desc = rootView.findViewById(R.id.starred_zero_desc);

        infoBox = rootView.findViewById(R.id.starred_info);



        previewList = rootView.findViewById(R.id.starred_preview_list);


        words = updateTitle(words);


        createPreviewList(words);


        return rootView;
    }




    private void createPreviewList(ArrayList<DataItem> dataItems) {


        previewList.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (DataItem dataItem: dataItems) {

            View item = inflater.inflate(R.layout.starred_list_item, null);
            TextView txt = item.findViewById(R.id.itemText);
            TextView  desc = item.findViewById(R.id.itemInfo);
            txt.setText( dataItem.item);
            desc.setText( dataItem.info);

            previewList.addView(item);
        }


    }


    private ArrayList<DataItem> updateTitle(ArrayList<DataItem> words) {


        words = dataManager.getStarredWords(false);

        if (words.size() < 1) {
            zero.setVisibility(View.VISIBLE);
            infoBox.setVisibility(View.GONE);

        } else {

            zero.setVisibility(View.GONE);
            infoBox.setVisibility(View.VISIBLE);

        }



        int limit = Constants.STARRED_LIMIT;

        String descTxt = String.format(getResources().getString(R.string.starred_words_info),  limit);

        String count = String.format("%d / %d", words.size(), limit);



        String zero = "0 / " + limit;

        countZero.setText(zero);
        desc.setText(descTxt);

        starredCount.setText(count);

        int displayLimit = 4;

        if (words.size() < displayLimit) displayLimit = words.size();

        words = new ArrayList<>(words.subList(0, displayLimit));

        return words;

    }


    private String getStarredWordsTitle(ArrayList<DataItem> words, int type) {
        String title= "";

        String divider = "; ";
        String ending = " ...";
        int limit = 7;

        if (type == 2) {
            divider  = "<br/></br>"; ending = "........"; limit = 5;
        }

        for (int i = 0; i<words.size(); i++) {

            String item = "<b>"+words.get(i).item + "</b><br>" + words.get(i).info;

            title = title + item;

            if (i < (words.size()-1) ) title = title +divider;

            if (i >=  (limit-1) ) break;
        }
        if (words.size() > limit ) title += ending;
        return title;
    }


    @Override
    public void onResume() {

        super.onResume();

        words = updateTitle(words);

        createPreviewList(words);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_starred, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.info_from_menu:
                showInfoDialog();
                return true;

            default:
                break;
        }
        return false;
    }

    public void showInfoDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.starred_menu_info)
                .setCancelable(true)
                .setNegativeButton(R.string.dialog_close_txt,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setMessage(R.string.starred_words_info);
        AlertDialog alert = builder.create();
        alert.show();

    }







}
