package com.online.languages.study.studymaster.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
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
import com.online.languages.study.studymaster.adapters.StarredTabsPagerAdapter;
import com.online.languages.study.studymaster.data.DataFromJson;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.online.languages.study.studymaster.Constants.STARRED_TAB_ACTIVE;


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

    ViewPager tabsPager;
    TabLayout tabLayout;
    StarredTabsPagerAdapter tabsAdapter;

    View starredList;
    View starredTabs;

    SharedPreferences appSettings;
    int activeTab;


    public StarredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_starred, container, false);


        appSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        activeTab = appSettings.getInt(STARRED_TAB_ACTIVE, 0);

        starredList = rootView.findViewById(R.id.starredList);
        starredTabs = rootView.findViewById(R.id.starredTabs);

        text = rootView.findViewById(R.id.starredWords);
        dataManager = new DataManager(getActivity());
        starredCount = rootView.findViewById(R.id.starred_count);

        zero = rootView.findViewById(R.id.starred_zero);
        countZero = rootView.findViewById(R.id.starred_count_preview);
        desc = rootView.findViewById(R.id.starred_zero_desc);

        infoBox = rootView.findViewById(R.id.starred_info);

        previewList = rootView.findViewById(R.id.starred_preview_list);

        words = updateTitle(words);
        checkTabsDisplay();

        createPreviewList(words);



        return rootView;
    }

    private void checkTabsDisplay() {
        ArrayList<DataItem> dataFromGallery = dataManager.getStarredWords(2,false);
        if (dataFromGallery.size() > 0) {
            starredList.setVisibility(View.GONE);
            starredTabs.setVisibility(View.VISIBLE);
        }
    }

    private int getActiveTabNum() {
        int act = appSettings.getInt(STARRED_TAB_ACTIVE, 0);
        //Toast.makeText(getActivity(), "Active tab: "+ activeTab, Toast.LENGTH_SHORT).show();
        return act;
    }

    private void setStarredTab(int tab) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putInt(STARRED_TAB_ACTIVE, tab);
        editor.apply();

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        tabLayout.addTab(tabLayout.newTab().setText("Записи"));
        tabLayout.addTab(tabLayout.newTab().setText("Галерея"));


        tabsPager = view.findViewById(R.id.tabContainer);
        tabsAdapter = new StarredTabsPagerAdapter(getChildFragmentManager(), 2 );

        tabsPager.setAdapter(tabsAdapter);

        tabsPager.setOffscreenPageLimit(2);

        tabsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                setStarredTab(tab.getPosition());
                tabsPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                tabsPager.setCurrentItem(activeTab, false);
            }
        }, 100);




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

    public void updateTabName(int tab, int count) {
        if (tab == 1) {
            tabLayout.getTabAt(0).setText("Записи ("+count+")");
        }

        if (tab == 2) {
            tabLayout.getTabAt(1).setText("Галерея ("+count+")");
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


    @Override
    public void onResume() {
        super.onResume();
        words = updateTitle(words);
        createPreviewList(words);
        checkTabsDisplay();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
           if (tabsPager !=null) tabsPager.setCurrentItem(getActiveTabNum(), true);
        }
    }




}
