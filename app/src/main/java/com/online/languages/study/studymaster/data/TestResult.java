package com.online.languages.study.studymaster.data;

import android.content.Context;

import com.online.languages.study.studymaster.DBHelper;

import java.util.ArrayList;

public class TestResult {


    private ArrayList<DataItem> dataItems;
    private Context context;
    private DBHelper dbHelper;

    private NavStructure navStructure;

    public ArrayList<ResultSection> sections;
    public ArrayList<ResultCategory> categories;


    public TestResult() {
    }


    public TestResult(Context _context, ArrayList<DataItem> _data) {
        dataItems = new ArrayList<>(_data);
        context  = _context;
        dbHelper = new DBHelper(context );
        DataFromJson dataFromJson = new DataFromJson(context);
        navStructure = dataFromJson.getStructure();

        sections = new ArrayList<>();
        categories = new ArrayList<>();

        getData();
    }



    private void getData() {

        dataItems = dbHelper.getTestDataByIds(dataItems);
        structureData();
    }


    public String prepareDisplay() {

        String str = "";

        for (ResultCategory category: categories) {

            str += "<br><br>" + category.title + "<br><br>";

            for (DataItem dataItem: category.dataItems) {

                str += "<b>" +dataItem.item +"</b> <br>"+ dataItem.info +"<br>";
            }

        }

        return str;
    }


    private void structureData() {

        for (NavSection navSection: navStructure.sections) {
            ResultSection section = new ResultSection();
            section.dataItems = new ArrayList<>();
            section.title = navSection.title;

            for (NavCategory navCategory: navSection.navCategories) {
                ResultCategory category = new ResultCategory();
                category.dataItems = new ArrayList<>();

                category.title = navCategory.title;

                for (DataItem dataItem: dataItems) {
                    if (dataItem.id.contains(navCategory.id)) {

                        section.dataItems.add(dataItem);
                        category.dataItems.add(dataItem);
                    }
                }

                if (category.dataItems.size() > 0) categories.add(category);
            }

            if (section.dataItems.size()>0) sections.add(section);
        }


    }


    public class ResultSection {
        ArrayList<DataItem> dataItems = new ArrayList<>();
        String title;

        public ResultSection() {
            dataItems = new ArrayList<>();
        }
    }


    public class ResultCategory {
        ArrayList<DataItem> dataItems = new ArrayList<>();
        String title;

        public ResultCategory() {
            dataItems = new ArrayList<>();
        }
    }




}
