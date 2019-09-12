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
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.online.languages.study.studymaster.AppStart;
import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.MainActivity;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.adapters.DividerItemDecoration;
import com.online.languages.study.studymaster.adapters.HomeCardRecycleAdapter;
import com.online.languages.study.studymaster.adapters.RoundedTransformation;
import com.online.languages.study.studymaster.data.DataFromJson;
import com.online.languages.study.studymaster.data.DataManager;
import com.online.languages.study.studymaster.data.NavStructure;
import com.online.languages.study.studymaster.data.Section;
import com.online.languages.study.studymaster.util.IabHelper;
import com.online.languages.study.studymaster.util.IabResult;
import com.online.languages.study.studymaster.util.Inventory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private HomeCardRecycleAdapter mAdapter;

    private boolean firstAdReceived = false;

    ArrayList<Section> sections;

    NavStructure navStructure;


    IabHelper mHelper;

    Boolean showAd;
    AdView mAdView;
    SharedPreferences appSettings;
    ImageView placeholder;
    View adContainer;
    View adSpace;
    View adCard;



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

        String theme = appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        mAdapter = new HomeCardRecycleAdapter(getActivity(), navStructure.sections, theme);

        placeholder = rootView.findViewById(R.id.placeholder);
        adContainer = rootView.findViewById(R.id.adContainer);
        adSpace = rootView.findViewById(R.id.adSpace);
        adCard = rootView.findViewById(R.id.adCard);


        int rowsNum = 1;

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), rowsNum, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration( new DividerItemDecoration(getActivity()) );
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



        showAd =false;

        mAdView = rootView.findViewById(R.id.adView);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {  // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {  // Code to be executed when an ad request fails.
                showBanner();
            }
        });


        checkAdShow();

        Picasso.with(getActivity() )
                .load("file:///android_asset/history.jpg")
                .fit()
                .centerCrop()
                .into(placeholder);
        return rootView;
    }

    private void checkAdShow() {
        showAd = appSettings.getBoolean(Constants.SET_SHOW_AD, false);


        SharedPreferences mLaunches = getActivity().getSharedPreferences(AppStart.APP_LAUNCHES, Context.MODE_PRIVATE);
        int launchesNum = mLaunches.getInt(AppStart.LAUNCHES_NUM, 0);

        if (launchesNum < 2) showBanner();
        else  manageAd();


    }

    private void showAdCard() {

        adContainer.setVisibility(View.VISIBLE);

        adCard.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        int mHeight = adCard.getHeight();
                        adCard.getViewTreeObserver().removeOnGlobalLayoutListener( this );
                        adSpace.setPadding(0, (mHeight-1), 0, 0);
                    }

                });

    }

    private void showBanner() {
        showAdCard();
        mAdView.setVisibility(View.GONE);
        placeholder.setAlpha(0f);
        placeholder.setVisibility(View.VISIBLE);
        placeholder.animate().alpha(1.0f).setDuration(150);

    }


    public void manageAd() {

        if (showAd) {

            showAdCard();

            String admob_ap_id = getResources().getString(R.string.admob_ap_id);
            MobileAds.initialize(getActivity().getApplicationContext(), admob_ap_id);

            final AdRequest adRequest;

            if (Constants.DEBUG) {
                adRequest = new AdRequest.Builder()
                        .addTestDevice("721BA1B0D2D410F1335955C3EC0C8B71")
                        .addTestDevice("725EEA094EAF285D1BD37D14A7F78C90")
                        .addTestDevice("0B44FDBCD710428A565AA061F2BD1A98")
                        .addTestDevice("88B83495F2CC0AF4C2C431655749C546")
                        .build();
            } else {
                adRequest = new AdRequest.Builder().build();
            }

            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(adRequest);

        } else {
            adSpace.setPadding(0, 0, 0, 0);
            adContainer.setVisibility(View.GONE);
            mAdView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {

        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
            if (!showAd) checkAdShow();
        }
    }

    @Override
    public void onDestroy() {

        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
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
