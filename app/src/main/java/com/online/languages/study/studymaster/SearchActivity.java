package com.online.languages.study.studymaster;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.adapters.CustomSectionAdapter;
import com.online.languages.study.studymaster.adapters.SearchDataAdapter;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.NavStructure;

import java.util.ArrayList;

import static com.online.languages.study.studymaster.Constants.GALLERY_TAG;


public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener {


    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    DBHelper dbHelper;

    SearchDataAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<DataItem> data;
    ArrayList<DataItem> displayData;

    int moreDataCoount = 0;
    Boolean full_version;

    SearchView searchView;

    View card;
    TextView result;
    TextView loadMoreTxt;

    NavStructure navStructure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        full_version = appSettings.getBoolean(Constants.SET_VERSION_TXT, false);


        navStructure = getIntent().getParcelableExtra(Constants.EXTRA_NAV_STRUCTURE);
        navStructure.getUniqueCats();


        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("");

        dbHelper = new DBHelper(this);

        card = findViewById(R.id.card);
        result = findViewById(R.id.searcTxt);

        loadMoreTxt = findViewById(R.id.loadMoreTxt);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setSelected(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                View animObj = view.findViewById(R.id.animObj);

                onItemClick(animObj, position);
                searchView.clearFocus();

            }
            @Override
            public void onLongClick(View view, int position) {

               if (full_version) changeStarred(position);

            }
        }));


        NestedScrollView mNestedScrollView  = findViewById(R.id.scrollView);


        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            int scrollPos = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (searchView.hasFocus()) searchView.clearFocus();
            }
        });

    }

    public void changeStarred(int position){   /// check just one item

        DataItem dataItem = data.get(position);

        String id = dataItem.id;


        String filter = "";
        if (dataItem.filter.contains(GALLERY_TAG)) filter = GALLERY_TAG;

        Boolean starred = dbHelper.checkStarred(id );

        int status = dbHelper.setStarred(id, !starred, filter); // id to id

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibLen = 30;

        if (status == 0) {
            Toast.makeText(this, R.string.starred_limit, Toast.LENGTH_SHORT).show();
            vibLen = 300;
        }

        checkStarred(position);

        assert v != null;
        v.vibrate(vibLen);
    }


    public void checkStarred(final int result){   /// check just one item

        data = dbHelper.checkStarredList(data);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(result);
            }
        }, 200);
    }



    private void onItemClick(final View view, final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAlertDialog(view, position);
            }
        }, 50);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == CatActivity.RESULT_OK){

                int result=data.getIntExtra("result", -1);

                checkStarred(result);
            }
        }
    }


    public void showAlertDialog(View view, int position) {

        Intent intent = new Intent(SearchActivity.this, ScrollingActivity.class);
        intent.putExtra("id", data.get(position).id );
        intent.putExtra("position", position);
        intent.putExtra("item", data.get(position));
        intent.putExtra("source", 1);

        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.slide_in_down, 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchMenuItem = menu.findItem(R.id.search);

        searchView = (SearchView) searchMenuItem.getActionView();

        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconified(false);
        searchView.requestFocus();

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.length() < 2 ) {
            adapter = new SearchDataAdapter(this, new ArrayList<DataItem>(), themeTitle);
            recyclerView.setAdapter(adapter);
            result.setVisibility(View.GONE);
            card.setVisibility(View.GONE);

        } else {
            results(newText);
        }

        return true;
    }

    public void focusLayout(View view) {
        searchView.clearFocus();
    }


    public void results(String query) {

        data = dbHelper.searchData(navStructure.categories, query);
        data = dbHelper.checkStarredList(data);

        int size = data.size();

        if (size == 0) {
            result.setVisibility(View.VISIBLE);

            String str = String.format(getString(R.string.no_search_result), query);

            result.setText(Html.fromHtml(str));

            card.setVisibility(View.GONE);

        } else {
            result.setVisibility(View.GONE);
            card.setVisibility(View.VISIBLE);
        }

        int limit = 15;

        displayData = new ArrayList<>(data);

        if (size > limit) {

            displayData = new ArrayList<>(data.subList(0, limit));
        }

        adapter = new SearchDataAdapter(this, displayData, themeTitle);
        recyclerView.setAdapter(adapter);

        manageMoreButton();

    }

    private void manageMoreButton() {

        moreDataCoount = displayLoadMore( displayData.size() ,  data.size() );

        int dif = data.size() - displayData.size();

        if (moreDataCoount > 0) {
            loadMoreTxt.setVisibility(View.VISIBLE);
            loadMoreTxt.setText(String.format(getString(R.string.load_more), moreDataCoount, dif));

        } else {

            loadMoreTxt.setVisibility(View.GONE);
        }
    }

    public void loadMore(View view) {

        int toInd = displayData.size() + moreDataCoount;

        if (toInd > data.size()) toInd = data.size();

        displayData.addAll( new ArrayList<>( data.subList(displayData.size(), toInd ) ) );

        adapter.notifyItemRangeInserted(displayData.size(), moreDataCoount);

        manageMoreButton();
    }


    private int displayLoadMore(int currentCount,  int dataSize) {


        int load = 0;

        int step = 10;

        int dif =  dataSize - currentCount;

        if (dif > 0) {

            if (dif > step) {
                load = step;
            } else {
                load = dif;
            }
        }

        return load;
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
