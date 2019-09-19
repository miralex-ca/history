package com.online.languages.study.studymaster.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ViewSection {

    public String title;
    public String desc;
    public String image;
    public int progress = 0;
    public String type;

    private Context context;

    private DataManager dataManager;

    private NavSection navSection;

    public ArrayList <ViewCategory> categories = new ArrayList<>();

    public ArrayList <ViewCategory> topCategories = new ArrayList<>();


    public ViewSection() {
    }

    public ViewSection(Context _context, NavSection _navSection, ArrayList<NavCategory> cats) {

        navSection = _navSection;

        title = navSection.title;
        desc = navSection.desc;
        image = navSection.image;

        context = _context;

        dataManager = new DataManager(context);

        for (NavCategory navCategory: cats) {

            ViewCategory viewCategory = new ViewCategory(navCategory);

            if (navCategory.type.equals("group")) {
                viewCategory.subgroup = countGroupByID(navSection, navCategory.id);
            }


            categories.add(viewCategory );



        }
    }


    private int countGroupByID(NavSection navSection, String catID) {
        int count = 0;
        for (NavCategory navCategory: navSection.navCategories) {
            if (navCategory.parent.equals(catID)) {
                count ++;
            }
        }
        return count;
    }

    public void getProgress() {

        categories = getCatProgress(navSection, categories);
       if (topCategories.size() > 0) topCategories = getCatProgress(navSection, topCategories);
    }


    private ArrayList<ViewCategory> getCatProgress(NavSection navSection, ArrayList<ViewCategory> cats) {

        Map<String, String> map =  dataManager.getCatProgress( navSection.catIdList ); //

        cats = getViewCatsResults(cats, map);

        return cats;

    }

    private ArrayList<ViewCategory> getViewCatsResults(ArrayList<ViewCategory> cats, Map<String, String> map) {

        for (ViewCategory viewCategory: cats) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String id = entry.getKey();
                String result = entry.getValue();

                if (id.matches(viewCategory.id)) {
                    viewCategory.progress = Integer.valueOf(result);
                }
            }
        }

        return cats;
    }


    private ArrayList<NavCategory> getCatsTestsArray(ArrayList<NavCategory> categories, Map<String, String> map) {

        for (NavCategory navCategory: categories) {
            int catProgress = 0;
            navCategory.tests = new ArrayList<>();

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String id = entry.getKey();
                String result = entry.getValue();

                if (id.matches(navCategory.id+".*")) {
                    navCategory.tests.add( Integer.valueOf(result) );
                    catProgress += Integer.valueOf(result);
                }
            }

            if (navCategory.tests.size() != 0) navCategory.progress = catProgress / navCategory.tests.size();

            progress += navCategory.progress;
        }

        progress = progress / categories.size();

      //  Toast.makeText(context, "Res: " + progress, Toast.LENGTH_SHORT).show();

        return categories;
    }


}
