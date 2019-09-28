package com.online.languages.study.studymaster.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.online.languages.study.studymaster.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class DataManager {

    private Context context;

    public DBHelper dbHelper;


    public DataManager(Context _context) {
        context = _context;
        dbHelper = new DBHelper(context);
    }


    public ArrayList<DataItem> getCatDBList(String cat) {
        return dbHelper.getCatByTag(cat);
    }

    public ArrayList<DataItem> getSectionDBList(NavSection navSection) {
        return dbHelper.getAllDataItems( navSection.uniqueCategories );
    }


    public ArrayList<DataItem> checkDataItemsData(ArrayList<DataItem> dataItems) {
        return dbHelper.checkStarredList(dataItems);
    }

    public boolean checkStarStatusById(String id) {
        return dbHelper.checkStarred(id);
    }



    public DetailItem getDetailFromDB(String id) {
        return dbHelper.getDetailById(id);
    }

    public DataItem getDataItemFromDB(String id) {
        return dbHelper.getDataItemById(id);
    }



    public ArrayList<DataItem> getStarredWords(Boolean sort) {
        return getStarredWords(1, sort);
    }


    public ArrayList<DataItem> getStarredWords(int type, Boolean sort) {

        SharedPreferences appSettings = PreferenceManager.getDefaultSharedPreferences(context);

        String sortType = appSettings.getString("starred_sort_type", "0");

        if (!sort) sortType = "0";

        ArrayList<DataItem> dataItems;

        if (type == 2) dataItems = dbHelper.getStarredFromDB(2);
        else dataItems = dbHelper.getStarredFromDB();

        if (sortType.equals("0")) {
            Collections.sort(dataItems, new TimeStarredComparator());
        } else if (sortType.equals("1")) {
            Collections.sort(dataItems, new TimeStarredComparator());
            Collections.reverse(dataItems);
        }
        return dataItems;
    }


    public ArrayList<DataItem> getCatCustomList(ArrayList<NavCategory> categories, int type) {

        ArrayList<DataItem> dataItems =  dbHelper.getDataItemsByCats( categories );

        ArrayList<DataItem> resultDataItems = new ArrayList<>();

        for (DataItem dataItem: dataItems) {
            if (type == 0) { // studied
                if (dataItem.rate > 2) resultDataItems.add(dataItem);
            } else if (type == 1) { // familiar
                if (dataItem.rate > 0) resultDataItems.add(dataItem);
            } else if (type == 2) { // unknown
                if (dataItem.rate < 1) resultDataItems.add(dataItem);
            }
        }

        if (type == 1) {
            Collections.sort(resultDataItems, new ScoreCountComparator());
        }

        return resultDataItems;

    }


    public UserStatsData getSectionsDataFromDB(UserStatsData userStatsData) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Section section: userStatsData.sectionsDataList) {



            section = dbHelper.selectSectionDataFromDB(db, section);

            section.calculateProgress();
        }

        db.close();
        return userStatsData;

    }




    public ArrayList<DataItem> getCatCustomList(String cat, int type) {


        ArrayList<DataItem> dataItems = getCatDBList(cat);
        ArrayList<DataItem> resultDataItems = new ArrayList<>();

        ArrayList<DataItem> helperDataItems = new ArrayList<>();

        for (DataItem dataItem: dataItems) {
            if (type == 0) { // studied
                if (dataItem.rate > 2) resultDataItems.add(dataItem);
            } else if (type == 1) { // familiar
                if (dataItem.rate > 0 && dataItem.rate < 3) resultDataItems.add(dataItem);

                if (dataItem.rate > 2) helperDataItems.add(dataItem);

            } else if (type == 2) { // unknown
                if (dataItem.rate < 1) resultDataItems.add(dataItem);
            }
        }

        if (type == 1 ) resultDataItems.addAll(helperDataItems);


        return resultDataItems;
    }

    public Map<String, String> getCatProgress(ArrayList<String> catIds) {

       return dbHelper.checkCatProgressDB(catIds);
    }


    private class TimeStarredComparator implements Comparator<DataItem> {
        @Override
        public int compare(DataItem o1, DataItem o2) {
            return o1.starred_time <= o2.starred_time ? 1 : -1;
        }
    }

    private class ScoreCountComparator implements Comparator<DataItem> {
        @Override
        public int compare(DataItem o1, DataItem o2) {
            return ( o1.rate - o2.rate);
        }
    }


}
