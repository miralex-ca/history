package com.online.languages.study.studymaster.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.MainActivity;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.adapters.HomeCardRecycleAdapter;
import com.online.languages.study.studymaster.data.DataFromJson;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.Section;

import java.util.ArrayList;

import static com.online.languages.study.studymaster.Constants.HOME_CARDS;


public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView recyclerViewCards;

    private HomeCardRecycleAdapter mAdapter;

    private boolean firstAdReceived = false;

    ArrayList<Section> sections;

    NavStructure navStructure;

    SharedPreferences appSettings;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        appSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        NavStructure navStructure = getArguments().getParcelable("structure");

        getSectionsData();

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerViewCards = rootView.findViewById(R.id.recycler_view_cards);

        String theme = appSettings.getString("theme", Constants.SET_THEME_DEFAULT);


        int recycleType = 1;
        if (HOME_CARDS) {
            recyclerView.setVisibility(View.GONE);
            recyclerViewCards.setVisibility(View.VISIBLE);
            recycleType = 2;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewCards.setVisibility(View.GONE);
        }

        mAdapter = new HomeCardRecycleAdapter(getActivity(), navStructure.sections, theme, recycleType);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                onGridClick(view, position);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        RecyclerView.LayoutManager mLayoutManagerCards = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerViewCards.setLayoutManager(mLayoutManagerCards);
        recyclerViewCards.setAdapter(mAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerViewCards, false);

        recyclerViewCards.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewCards, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                onGridClick(view, position);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return rootView;
    }


    private void getSectionsData() {

        DataFromJson dataFromJson = new DataFromJson(getActivity());

        sections = dataFromJson.getSectionsList();

        navStructure = dataFromJson.getStructure();

    }


    private void onGridClick(final View view, final int position) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // String id =  vocab.sectionTags.get(act);

                ((MainActivity)getActivity()).openCatActivity(view, position);

            }
        }, 80);
    }

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
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
