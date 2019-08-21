package com.online.languages.study.studymaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.online.languages.study.studymaster.CatActivity;
import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.adapters.ContentAdapter;
import com.online.languages.study.studymaster.adapters.DividerItemDecoration;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DataManager;

import java.util.ArrayList;


public class CatTabFragment1 extends Fragment {


    ArrayList<DataItem> data = new ArrayList<>();
    ContentAdapter adapter;
    DataManager dataManager;

    SharedPreferences appSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cat_1, container, false);


        dataManager = new DataManager(getActivity());
        appSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        int showStatus = Integer.valueOf(appSettings.getString("show_status", Constants.STATUS_SHOW_DEFAULT));

        String forceStatus = "no";
        if (getActivity().getIntent().hasExtra(Constants.EXTRA_FORCE_STATUS)) {
            forceStatus = getActivity().getIntent().getStringExtra(Constants.EXTRA_FORCE_STATUS);
        }

        if (forceStatus.equals("always")) showStatus = 2;

        String id = CatActivity.categoryID;

        data = dataManager.getCatDBList(id);

        data = insertDivider(data);

        //DataItem d = data.get(0);
        // String s  = "ID: " + d.id + "; item: " + d.item + " ; desc: "+ d.info;
        // Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();


        String theme = appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        RecyclerView recyclerView = rootView.findViewById(R.id.my_recycler_view);
        adapter = new ContentAdapter(getActivity(), data, showStatus, theme);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration( new DividerItemDecoration(getActivity()) );
        recyclerView.setAdapter(adapter);

        openView(recyclerView);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                View animObj = view.findViewById(R.id.animObj);

                // Toast.makeText(getActivity(), "Dialog OnClick", Toast.LENGTH_SHORT).show();

                onItemClick(animObj, position);

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return rootView;
    }


    private ArrayList<DataItem> insertDivider(ArrayList<DataItem> data) {



        ArrayList<DataItem> list = new ArrayList<>();

        for (DataItem dataItem: data) {

            if (!dataItem.divider.equals("no")) {

                DataItem item = new DataItem();

                item.item = dataItem.divider;
                item.type = "divider";

                list.add(item);
            }

            list.add(dataItem);

        }


        return list;
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
                ((CatActivity)getActivity()).showAlertDialog(view, position);
            }
        }, 50);
    }



    @Override
    public void onResume() {
        super.onResume();
    }



    public void checkDataList() {   /// check all items

        // Toast.makeText(getActivity(), "Update list", Toast.LENGTH_SHORT).show();

        data = dataManager.checkDataItemsData(data);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 80);
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
