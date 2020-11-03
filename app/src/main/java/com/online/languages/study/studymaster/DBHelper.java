package com.online.languages.study.studymaster;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import com.online.languages.study.studymaster.adapters.InfoNotesManager;
import com.online.languages.study.studymaster.data.Category;
import com.online.languages.study.studymaster.data.DataFromJson;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DataManager;
import com.online.languages.study.studymaster.data.DetailFromJson;
import com.online.languages.study.studymaster.data.DetailItem;
import com.online.languages.study.studymaster.data.NavCategory;
import com.online.languages.study.studymaster.data.NoteData;
import com.online.languages.study.studymaster.data.Section;
import com.online.languages.study.studymaster.data.UserStats;
import com.online.languages.study.studymaster.data.UserStatsData;
import com.online.languages.study.studymaster.files.DBImport;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.online.languages.study.studymaster.Constants.GALLERY_TAG;
import static com.online.languages.study.studymaster.Constants.NOTE_ARCHIVE;
import static com.online.languages.study.studymaster.Constants.NOTE_TAG;
import static com.online.languages.study.studymaster.Constants.STARRED_TAB_ACTIVE;
import static com.online.languages.study.studymaster.Constants.TAB_GALLERY;
import static com.online.languages.study.studymaster.Constants.TAB_ITEMS;


public class DBHelper extends SQLiteOpenHelper {

    private Context cntx;
    SharedPreferences appSettings;
    private int MAX_SCORE = 4;
    private static final int DATABASE_VERSION = BuildConfig.VERSION_CODE;
    public static final String DATABASE_NAME = BuildConfig.DBNAME;

    public static final String TABLE_CAT_DATA = "cat_data";
    public static final String TABLE_USER_DATA = "user_items_data";
    public static final String TABLE_TESTS_DATA = "tests_data";
    public static final String TABLE_ITEMS_DATA = "items_data";
    public static final String TABLE_DETAILS_DATA = "details_data";
    public static final String TABLE_NOTES_DATA = "notes_data";

    // common
    private static final String KEY_PRIMARY_ID = "id";

    //// items table

    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_ITEM_TITLE = "item_title";
    private static final String KEY_ITEM_DESC = "item_desc";
    private static final String KEY_ITEM_IMAGE = "item_image";
    private static final String KEY_ITEM_INFO_1 = "item_info_1";
    private static final String KEY_ITEM_DIVIDER = "item_divider";
    private static final String KEY_ITEM_FILTER = "item_filter";
    private static final String KEY_ITEM_MODE = "item_mode";


    //// cats table
    private static final String KEY_CAT_ID = "cat_id";
    private static final String KEY_CAT_PROGRESS = "progress";

    //// test table
    private static final String KEY_TEST_TAG = "tag";
    private static final String KEY_TEST_PROGRESS = "progress";
    private static final String KEY_TEST_TIME = "test_time";


    //// dataItems and phrases tables columns
    private static final String KEY_USER_ITEM_ID = "user_item_id";

    private static final String KEY_ITEM_INFO = "item_info";
    private static final String KEY_ITEM_PROGRESS = "item_progress";
    private static final String KEY_ITEM_ERRORS = "item_errors";
    private static final String KEY_ITEM_SCORE = "item_score";
    private static final String KEY_ITEM_STATUS = "item_status";
    private static final String KEY_ITEM_STARRED = "item_starred";
    private static final String KEY_ITEM_TIME = "item_time";
    private static final String KEY_ITEM_TIME_STARRED = "item_time_starred";
    private static final String KEY_ITEM_TIME_ERROR = "item_time_error";

    //// detail table columns
    private static final String KEY_DETAIL_ID = "detail_id";
    private static final String KEY_DETAIL_TITLE = "detail_tile";
    private static final String KEY_DETAIL_DESC = "detail_desc";
    private static final String KEY_DETAIL_IMAGE = "detail_image";
    private static final String KEY_DETAIL_IMG_INFO = "detail_img_imfo";

    //// notes table columns
    private static final String KEY_NOTE_PRIMARY_ID = "note_primary_id";
    private static final String KEY_NOTE_ID = "note_id";
    private static final String KEY_NOTE_TITLE = "note_title";
    private static final String KEY_NOTE_TEXT = "note_text";
    private static final String KEY_NOTE_ICON = "note_icon";
    private static final String KEY_NOTE_INFO = "note_info";
    private static final String KEY_NOTE_STATUS = "note_status";
    private static final String KEY_NOTE_TYPE = "note_type";
    private static final String KEY_NOTE_PARAMS = "note_params";
    private static final String KEY_NOTE_FILTER = "note_filter";
    private static final String KEY_NOTE_PARENT = "note_parent";
    private static final String KEY_NOTE_ORDER = "note_order";
    private static final String KEY_NOTE_CREATED = "note_created";
    private static final String KEY_NOTE_UPDATED = "note_updated";
    private static final String KEY_NOTE_UPDATED_SORT = "note_updated_sort";


    private static final String TABLE_ITEM_STRUCTURE  = "("
            + KEY_PRIMARY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ITEM_ID + " TEXT,"
            + KEY_ITEM_TITLE + " TEXT,"
            + KEY_ITEM_DESC + " TEXT,"
            + KEY_ITEM_IMAGE + " TEXT,"
            + KEY_ITEM_INFO_1+ " TEXT,"
            + KEY_ITEM_FILTER + " TEXT,"
            + KEY_ITEM_MODE + "  INTEGER DEFAULT 0,"
            + KEY_ITEM_DIVIDER + " TEXT"
            + ")";


    private static final String TABLE_USER_STRUCTURE = "("
            + KEY_USER_ITEM_ID + " TEXT,"
            + KEY_ITEM_INFO + " TEXT,"
            + KEY_ITEM_PROGRESS + " INTEGER,"
            + KEY_ITEM_ERRORS + " INTEGER DEFAULT 0,"
            + KEY_ITEM_SCORE + " INTEGER DEFAULT 0,"
            + KEY_ITEM_STATUS + " INTEGER DEFAULT 0,"
            + KEY_ITEM_STARRED + " INTEGER DEFAULT 0,"
            + KEY_ITEM_TIME + " INTEGER,"
            + KEY_ITEM_TIME_STARRED + " INTEGER,"
            + KEY_ITEM_TIME_ERROR + " INTEGER"
            + ")";

    private static final String TABLE_DETAIL_STRUCTURE = "("
            + KEY_DETAIL_ID + " TEXT,"
            + KEY_DETAIL_TITLE + " TEXT,"
            + KEY_DETAIL_DESC + " TEXT,"
            + KEY_DETAIL_IMAGE + " TEXT,"
            + KEY_DETAIL_IMG_INFO + " TEXT"
            + ")";


    private static final String TABLE_TEST_STRUCTURE = "("
            + KEY_TEST_TAG + " TEXT,"
            + KEY_TEST_PROGRESS + " INTEGER,"
            + KEY_TEST_TIME + " INTEGER"
            + ")";

    private static final String TABLE_NOTES_STRUCTURE = "("
            + KEY_NOTE_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_NOTE_ID + " TEXT,"
            + KEY_NOTE_TITLE + " TEXT,"
            + KEY_NOTE_TEXT + " TEXT,"
            + KEY_NOTE_ICON+ " TEXT,"
            + KEY_NOTE_INFO + " TEXT,"
            + KEY_NOTE_STATUS + " TEXT,"
            + KEY_NOTE_TYPE + " TEXT,"
            + KEY_NOTE_PARAMS + " TEXT,"
            + KEY_NOTE_FILTER + " TEXT,"
            + KEY_NOTE_PARENT + " TEXT,"
            + KEY_NOTE_ORDER + " INTEGER,"
            + KEY_NOTE_CREATED + " INTEGER,"
            + KEY_NOTE_UPDATED + " INTEGER,"
            + KEY_NOTE_UPDATED_SORT + " INTEGER"
            + ")";


    private static final String TABLE_ITEMS_STRUCTURE = TABLE_ITEMS_DATA + TABLE_ITEM_STRUCTURE;

    private static final String TABLE_USER_ITEMS_STRUCTURE = TABLE_USER_DATA + TABLE_USER_STRUCTURE;

    private static final String TABLE_DETAILS_STRUCTURE = TABLE_DETAILS_DATA + TABLE_DETAIL_STRUCTURE;



    private static final String CREATE_USER_ITEMS_TABLE = "CREATE TABLE " + TABLE_USER_ITEMS_STRUCTURE;

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS_STRUCTURE;


    private static final String CREATE_USER_ITEMS_TABLE_IF_EXISTS = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_ITEMS_STRUCTURE;

    private static final String CREATE_ITEMS_TABLE_IF_EXISTS = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS_STRUCTURE;

    private static final String CREATE_DETAILS_TABLE_IF_EXISTS = "CREATE TABLE IF NOT EXISTS " + TABLE_DETAILS_STRUCTURE;


    private String CREATE_CATDATA_TABLE = "CREATE TABLE " + TABLE_CAT_DATA + "("
            + KEY_CAT_ID + " TEXT,"
            + KEY_CAT_PROGRESS + " INTEGER"
            + ")";


    private static final String CREATE_TESTS_TABLE = "CREATE TABLE " + TABLE_TESTS_DATA + TABLE_TEST_STRUCTURE;
    private static final String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES_DATA + TABLE_NOTES_STRUCTURE;

    private int data_mode = 0;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        cntx = context;

        appSettings = PreferenceManager.getDefaultSharedPreferences(context);
        data_mode = Integer.parseInt(appSettings.getString("data_mode", "2"));

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        populateDB(db);

        db.execSQL(CREATE_CATDATA_TABLE);
        db.execSQL(CREATE_USER_ITEMS_TABLE);
        db.execSQL(CREATE_TESTS_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);

        InfoNotesManager infoNotesManager = new InfoNotesManager(cntx);
        infoNotesManager.postStartNotes(DBHelper.this, db);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        populateDB(db);
        sanitizeDB(db);

        if (newVersion > 2) {
            db.execSQL(CREATE_NOTES_TABLE); // creates tables if not exist
        }


        InfoNotesManager infoNotesManager = new InfoNotesManager(cntx);
        infoNotesManager.postUpdateNotes(DBHelper.this, db, newVersion);

    }


    public void populateDB() {

        SQLiteDatabase db = this.getWritableDatabase();

        populateDB(db);

        db.close();

    }

    private void populateDB(SQLiteDatabase db) {

        DataFromJson dataFromJson= new DataFromJson(cntx);
        ArrayList<DataItem> allItems = dataFromJson.getAllData();


        DetailFromJson detailFromJson= new DetailFromJson(cntx);
        ArrayList<DetailItem> allDetails = detailFromJson.getAllData();


        db.beginTransaction();


        try {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS_DATA);
            db.execSQL(CREATE_ITEMS_TABLE_IF_EXISTS);

            for (DataItem item: allItems) {

                ContentValues values = new ContentValues();
                values.put(KEY_ITEM_ID, item.id);
                values.put(KEY_ITEM_TITLE, item.item);
                values.put(KEY_ITEM_DESC, item.info);
                values.put(KEY_ITEM_IMAGE, item.image);
                values.put(KEY_ITEM_INFO_1, item.item_info_1);
                values.put(KEY_ITEM_DIVIDER, item.divider);
                values.put(KEY_ITEM_FILTER, item.filter);
                values.put(KEY_ITEM_MODE, item.mode);

                db.insert(TABLE_ITEMS_DATA, null, values);
            }


            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }


        db.beginTransaction();

        try {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS_DATA);
            db.execSQL(CREATE_DETAILS_TABLE_IF_EXISTS);

            for (DetailItem item: allDetails) {
                ContentValues values = new ContentValues();
                values.put(KEY_DETAIL_ID, item.id);
                values.put(KEY_DETAIL_TITLE, item.title);
                values.put(KEY_DETAIL_DESC, item.desc);
                values.put(KEY_DETAIL_IMAGE, item.image);
                values.put(KEY_DETAIL_IMG_INFO, item.img_info);
                db.insert(TABLE_DETAILS_DATA, null, values);
            }


            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }



    }


    public void getUpdatedTable(SQLiteDatabase db) {

        db.beginTransaction();

        try {
            db.execSQL(CREATE_USER_ITEMS_TABLE_IF_EXISTS);

            db.execSQL("DROP TABLE IF EXISTS temp_" + TABLE_USER_DATA);

            List<String> columns = GetColumns(db, TABLE_USER_DATA);

            db.execSQL("ALTER TABLE " + TABLE_USER_DATA + " RENAME TO temp_" + TABLE_USER_DATA);

            db.execSQL(CREATE_USER_ITEMS_TABLE);

            columns.retainAll(GetColumns(db, TABLE_USER_DATA));

            String cols = dbJoin(columns, ",");

            db.execSQL(String.format(
                    "INSERT INTO %s (%s) SELECT %s from temp_%s",
                    TABLE_USER_DATA, cols, cols, TABLE_USER_DATA));

            db.execSQL("DROP TABLE temp_" + TABLE_USER_DATA);

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

    }

    private static List<String> GetColumns(SQLiteDatabase db, String tableName) {
        List<String> ar = null;
        Cursor c = null;
        try {
            c = db.rawQuery("select * from " + tableName + " limit 1", null);
            if (c != null) {
                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return ar;
    }

    private static String dbJoin(List<String> list, String delim) {
        StringBuilder buf = new StringBuilder();
        int num = list.size();
        for (int i = 0; i < num; i++) {
            if (i != 0)
                buf.append(delim);
            buf.append((String) list.get(i));
        }
        return buf.toString();
    }


    void setTestResult(String cat_id, int ex_type, int result, Boolean forceSave) {
        SQLiteDatabase db = this.getWritableDatabase();
        String action = "nothing";

        String ex_id = cat_id+"_"+ex_type;

        if (result > 100) result = 100;


        Cursor cursor = db.query(TABLE_TESTS_DATA,  null, KEY_TEST_TAG +" = ?",
                new String[] { ex_id }, null, null, null);

        if ( cursor.moveToFirst() ) {
            cursor.moveToFirst();
            int exSavedData = cursor.getInt(cursor.getColumnIndex(KEY_TEST_PROGRESS));
            if (result > exSavedData || forceSave) {
                action = "update";
            }
        } else {
            action = "insert";
        }
        ContentValues values = new ContentValues();
        values.put(KEY_TEST_TAG, ex_id);
        values.put(KEY_TEST_PROGRESS, result);
        values.put(KEY_TEST_TIME, System.currentTimeMillis() );

        if ( action.equals("update")) {
            db.update(TABLE_TESTS_DATA, values, KEY_TEST_TAG + " = ?", new String[]{ex_id});
        } else if (action.equals("insert")){
            db.insert(TABLE_TESTS_DATA, null, values);
        }

        //Toast.makeText(cntx, "Action: "+action + "; id: "+ ex_id + "; r: "+ result, Toast.LENGTH_SHORT).show();

        cursor.close();

        db.close();
    }


    public void updateCatResult(String cat_id, int tests_num) {
        SQLiteDatabase db = this.getWritableDatabase();
        setCatResult(db, cat_id, tests_num);
        db.close();
    }


    private void setCatResult(SQLiteDatabase db, String cat_id, int tests_num) {

        int progress = 0;
        String idPrefix = cat_id+"%";

        Cursor cursor = db.query(TABLE_TESTS_DATA,  null,
                KEY_TEST_TAG +" LIKE ?", new String[] { idPrefix },
                null, null, null);
        try {
            while (cursor.moveToNext()) {
                int catProgress = cursor.getInt(cursor.getColumnIndex(KEY_TEST_PROGRESS));
                progress += catProgress;
            }

            if (cursor.getCount() > 0) progress = progress / tests_num;

        } finally {
            cursor.close();
        }

        if (progress > 0) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_CAT_ID, cat_id);
            initialValues.put(KEY_CAT_PROGRESS, progress);



            Boolean exist = false;

            Cursor checkCursor = db.query(TABLE_CAT_DATA,  null,
                    KEY_CAT_ID +" = ?", new String[] { cat_id },
                    null, null, null);

            if (checkCursor.getCount() > 0 ) exist= true;

            checkCursor.close();

            if (exist) {
                db.update(TABLE_CAT_DATA, initialValues, KEY_CAT_ID+"=?", new String[] {cat_id});
            } else {
                db.insert(TABLE_CAT_DATA, null, initialValues);
            }

        }
    }


    public int deleteCatResult(String catId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int num = 0;

        String idPrefix = catId+"_%";

        db.delete(TABLE_TESTS_DATA, KEY_TEST_TAG +" LIKE ?", new String[]{idPrefix});

        ContentValues catValues = new ContentValues();
        catValues.put(KEY_CAT_PROGRESS, 0);
        db.update(TABLE_CAT_DATA, catValues, KEY_CAT_ID+"=?", new String[] {catId});

        // int num = db.delete(TABLE_USER_DATA, KEY_USER_ITEM_ID +" LIKE ?", new String[]{idPrefix});

        ArrayList<String> ids = new ArrayList<>();

        Cursor cursor = db.query(TABLE_USER_DATA,  null, KEY_USER_ITEM_ID +" LIKE ?",
                    new String[]{idPrefix}, null, null, null);


            while (cursor.moveToNext()) {
                String id = cursor.getString( (cursor.getColumnIndex(KEY_USER_ITEM_ID)));
                ids.add(id);
            }

        cursor.close();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_PROGRESS, 0);
        values.put(KEY_ITEM_ERRORS, 0);
        values.put(KEY_ITEM_SCORE, 0);
        values.put(KEY_ITEM_TIME, "");
        values.put(KEY_ITEM_TIME_ERROR, "");

        db.beginTransaction();

        try {

            for (String id: ids) {
                db.update(TABLE_USER_DATA, values, KEY_USER_ITEM_ID +" = ?", new String[]{id});
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }


        db.close();

        return num;
    }




    public int getTestResult(String exId) {
        int catResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_TESTS_DATA, new String[] { KEY_TEST_PROGRESS },
                KEY_TEST_TAG + " = ?", new String[]{exId}, null, null, null);
        if ( cursor.moveToFirst() ) {
            catResult = cursor.getInt(cursor.getColumnIndex(KEY_TEST_PROGRESS));
        }

        cursor.close();
        db.close();
        return catResult;
    }



    public void setWordResult(String w_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String action = "nothing";

        String table = TABLE_USER_DATA;

        Cursor cursor = db.query(TABLE_USER_DATA,  null, KEY_USER_ITEM_ID +" = ?",
                new String[] { w_id}, null, null, null);

        int w_progress = 1;
        int w_score = 1;
        long time = System.currentTimeMillis();
        int errors = 0;

        if ( cursor.moveToFirst() ) {

            cursor.moveToFirst();
            w_progress = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_PROGRESS));
            w_score = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SCORE));
            w_score = countScore(w_score, 1);
            w_progress++;
            action = "update";

            errors = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ERRORS));

            if (errors > 0) errors--;

            //Toast.makeText(cntx, "Error: " + errors, Toast.LENGTH_SHORT).show();


        } else {
            action = "insert";
        }

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ITEM_ID, w_id);
        values.put(KEY_ITEM_PROGRESS, w_progress);
        values.put(KEY_ITEM_SCORE, w_score);
        values.put(KEY_ITEM_TIME, time);

        values.put(KEY_ITEM_ERRORS, errors);


        if (w_score >= MAX_SCORE) values.put(KEY_ITEM_ERRORS, 0);

        if ( action.equals("update")) {
            db.update(table, values, KEY_USER_ITEM_ID +" = ?", new String[]{w_id});
        } else if (action.equals("insert")){
            db.insert(table, null, values);
        }

        cursor.close();
        db.close();
    }

    public void setError(String w_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String action = "nothing";

        String table = TABLE_USER_DATA;

        Cursor cursor = db.query(table,  null, KEY_USER_ITEM_ID +" = ?",
                new String[] { w_id}, null, null, null);


        int w_error = 1;
        int w_progress = 0;
        int w_score = 0;
        long time = System.currentTimeMillis();

        if ( cursor.moveToFirst() ) {
            cursor.moveToFirst();
            w_error = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ERRORS));
            w_progress = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_PROGRESS));
            w_error++;
            w_score = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SCORE));
            w_score = countScore(w_score, -1);
            action = "update";
        } else {
            action = "insert";
        }

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ITEM_ID, w_id);
        values.put(KEY_ITEM_PROGRESS, w_progress);
        values.put(KEY_ITEM_ERRORS, w_error);
        values.put(KEY_ITEM_SCORE, w_score);
        values.put(KEY_ITEM_TIME, time);
        values.put(KEY_ITEM_TIME_ERROR, time);

        if ( action.equals("update")) {
            db.update(table, values, KEY_USER_ITEM_ID +" = ? ", new String[]{w_id});
        } else if (action.equals("insert")){
            db.insert(table, null, values);
        }

        cursor.close();
        db.close();
    }


    private void setStarredTab(int type) {

        String tab = TAB_ITEMS;
        if (type == 1) tab = TAB_GALLERY;

        SharedPreferences.Editor editor = appSettings.edit();
        editor.putString(STARRED_TAB_ACTIVE, tab);
        editor.apply();

    }

    public int setStarred(String w_id, Boolean star) {

       return setStarred(w_id, star, "");
    }


    public int setStarred(String w_id, Boolean star, String _info) {
        SQLiteDatabase db = this.getWritableDatabase();
        String action = "nothing";
        int status = 1;

        int check = starredGroupSize(db, w_id, _info);

        // Toast.makeText(cntx, "Starred: "+ check, Toast.LENGTH_SHORT).show();


        if ( check < Constants.STARRED_LIMIT || !star) {

            Cursor cursor = db.query(TABLE_USER_DATA, null, KEY_USER_ITEM_ID + " = ?",
                    new String[]{w_id}, null, null, null);

            int w_progress = 0;
            int starred = 0;

            long time = System.currentTimeMillis();

            if (star) {
                starred = 1;
                if (_info.contains(GALLERY_TAG)) setStarredTab(1);
                else setStarredTab(0);
            }

            String info = _info;
            if (!star) info = "";

            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                w_progress = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_PROGRESS));
                action = "update";
            } else {
                action = "insert";
            }

            ContentValues values = new ContentValues();
            values.put(KEY_USER_ITEM_ID, w_id);
            values.put(KEY_ITEM_INFO, info);
            values.put(KEY_ITEM_PROGRESS, w_progress);
            values.put(KEY_ITEM_STARRED, starred);
            values.put(KEY_ITEM_TIME_STARRED, time);

            if (action.equals("update")) {
                db.update(TABLE_USER_DATA, values, KEY_USER_ITEM_ID + " = ? ", new String[]{w_id});
            } else if (action.equals("insert")) {
                db.insert(TABLE_USER_DATA, null, values);
            }
            cursor.close();

        } else {
            status = 0;
            //Toast.makeText(cntx, "Starred limit", Toast.LENGTH_SHORT).show();
        }
        db.close();

        return status;
    }


    private int starredGroupSize(SQLiteDatabase db, String id, String _filter) {


        DataManager dataManager = new DataManager(cntx, 1);


        StringBuilder conditionLike = new StringBuilder("");

        for (int i = 0; i < dataManager.navCategories.size(); i++) {

            String like = "a."+KEY_USER_ITEM_ID + " LIKE '" + dataManager.navCategories.get(i).id + "%' ";

            if (i != 0) {
                like = "OR " + like;
            }
            conditionLike.append(like);
        }


        String query = "SELECT * FROM "
                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"

                +" WHERE ("+conditionLike+") AND a." + KEY_ITEM_STARRED +" > ? AND a." + KEY_ITEM_INFO +" NOT LIKE ?";

        Cursor checkCursor = db.rawQuery(query, new String[]{"0", GALLERY_TAG});


        /*
        Cursor checkCursor = db.query(TABLE_USER_DATA,  null,
                KEY_USER_ITEM_ID + " LIKE ? AND " + KEY_ITEM_STARRED +" > ? AND " + KEY_ITEM_INFO +" NOT LIKE ?",
                new String[] {idPrefix, "0", GALLERY_TAG}, null, null, null);
*/


        if (_filter.contains(GALLERY_TAG)) {

            query = "SELECT * FROM "
                    +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                    +" b ON a.user_item_id=b.item_id"

                    +" WHERE a."+KEY_ITEM_STARRED +" > ? AND a." + KEY_ITEM_INFO +" LIKE ?";

            checkCursor = db.rawQuery(query, new String[]{"0", GALLERY_TAG});


            /*
            checkCursor = db.query(TABLE_USER_DATA,  null,
                    KEY_USER_ITEM_ID + " LIKE ? AND " + KEY_ITEM_STARRED +" > ? AND " + KEY_ITEM_INFO +" LIKE ?",
                    new String[] {idPrefix, "0", GALLERY_TAG}, null, null, null);
            */

        }


        int size = checkCursor.getCount();
        checkCursor.close();

        return size;
    }


    public Boolean checkStarred(String word_id) {
        Boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USER_DATA,  null, KEY_USER_ITEM_ID +" = ? ",
                new String[] {word_id}, null, null, null);

        if (cursor.moveToFirst() ) {
            cursor.moveToFirst();
            int current = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_STARRED));
            if (current > 0) status = true;
        }

        cursor.close();
        db.close();
        return status;
    }



    public ArrayList<DataItem> searchData(ArrayList<NavCategory> navCategories, String searchTerm) {
        SQLiteDatabase db = getReadableDatabase();


        StringBuilder conditionLike = new StringBuilder("");

        for (int i = 0; i < navCategories.size(); i++) {
            String like = KEY_ITEM_ID + " LIKE '" + navCategories.get(i).id + "%' ";

            if (i != 0) {
                like = "OR " + like;
            }
            conditionLike.append(like);
        }

        ArrayList<DataItem> items = new ArrayList<>();


        String query = "SELECT * FROM " +TABLE_ITEMS_DATA
                +" WHERE ("+conditionLike +")"
                +" AND " + "("+KEY_ITEM_TITLE+" LIKE '%"+searchTerm+"%' OR "+KEY_ITEM_DESC+" LIKE '%" + searchTerm+"%')";


        Cursor cursor = db.rawQuery(query, null);


        try {
            while (cursor.moveToNext()) {
                items.add(getSimpleItemFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }


        String notesQuery = "SELECT * FROM " +TABLE_NOTES_DATA
                +" WHERE  ("+KEY_NOTE_TITLE+" LIKE '%"+searchTerm+"%' OR "+KEY_NOTE_TEXT+" LIKE '%" + searchTerm+"%')"
                +"ORDER BY "+KEY_NOTE_UPDATED_SORT+ " DESC";


        Cursor notesCursor = db.rawQuery(notesQuery, null);

        try {
            while (notesCursor.moveToNext()) {
                items.add(getSimpleItemFromNoteCursor(notesCursor));
            }
        } finally {
            notesCursor.close();
        }



        db.close();

        return items;

    }

    private DataItem getSimpleItemFromNoteCursor(Cursor cursor) {

        DataItem dataItem = new DataItem();

        dataItem.id = cursor.getString(cursor.getColumnIndex(KEY_NOTE_ID));
        dataItem.item = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
        dataItem.info = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TEXT));
        dataItem.image = cursor.getString(cursor.getColumnIndex(KEY_NOTE_ICON));

        dataItem.db_filter = NOTE_TAG;
        dataItem.filter = NOTE_TAG;

        return dataItem;
    }


    public ArrayList<DataItem> getAllDataItems(ArrayList<NavCategory> navCategories) {
        ArrayList<DataItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();


        StringBuilder conditionLike = new StringBuilder("");

        for (int i = 0; i < navCategories.size(); i++) {

            String like = KEY_ITEM_ID + " LIKE '" + navCategories.get(i).id + "%' ";

            if (i != 0) {
                like = "OR " + like;
            }
            conditionLike.append(like);
        }

        String query = "SELECT * FROM " +TABLE_ITEMS_DATA
                +" WHERE "+conditionLike;


        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                items.add(getSimpleItemFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }

        db.close();

        return items;
    }



    public ArrayList<DataItem> getDataItemsByCats(ArrayList<NavCategory> navCategories) {


        ArrayList<DataItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();


        StringBuilder conditionLike = new StringBuilder("");


        for (int i = 0; i < navCategories.size(); i++) {

            String like = "a."+KEY_ITEM_ID + " LIKE '" + navCategories.get(i).id + "%' ";

            if (i != 0) {
                like = "OR " + like;
            }
            conditionLike.append(like);
        }


        String query = "SELECT * FROM "
                +TABLE_ITEMS_DATA +" a LEFT JOIN "+TABLE_USER_DATA +" b ON a.item_id=b.user_item_id"

                +" WHERE ("+ conditionLike +") "
                + " AND (a."+KEY_ITEM_MODE+" < "+data_mode+")" ;


        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                items.add(getItemFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }

        db.close();

        return items;
    }



    public ArrayList<DataItem> getTestDataByIds(ArrayList<DataItem> dataItems) {
        ArrayList<DataItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        for (DataItem item: dataItems) {

            Cursor cursor = db.query(TABLE_ITEMS_DATA,  null, KEY_ITEM_ID +" = ?",
                    new String[] { item.id}, null, null, null);

            try {
                while (cursor.moveToNext()) {

                    DataItem data = getSimpleItemFromCursor(cursor);
                    data.testError = item.testError;
                    items.add(data);
                }

            } finally {
                cursor.close();
            }
        }

        return items;
    }


    public ArrayList<DataItem> getDataItemsByCatIds(ArrayList<String> catIds) {

        ArrayList<DataItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();


        String limit = "";
        if (catIds.size() > 10) {
            limit = "ORDER BY RANDOM() LIMIT 15";
        }

        String columns = KEY_ITEM_ID +", "+ KEY_ITEM_TITLE + ", "+ KEY_ITEM_DESC+ ", "+ KEY_ITEM_INFO_1;


        db.beginTransaction();

        for (int i = 0; i < catIds.size(); i++) {

            String catQuery = "SELECT "+columns+" FROM " +TABLE_ITEMS_DATA
                    +" WHERE ("+KEY_ITEM_ID + " LIKE '" + catIds.get(i) + "%') "

                    + "AND ("+KEY_ITEM_MODE+" < "+data_mode+") "

                    +limit;

            Cursor cursor = db.rawQuery(catQuery, null);

            try {
                while (cursor.moveToNext()) {

                    DataItem dataItem = new DataItem();

                    dataItem.id = cursor.getString(cursor.getColumnIndex(KEY_ITEM_ID));
                    dataItem.item = cursor.getString(cursor.getColumnIndex(KEY_ITEM_TITLE));
                    dataItem.info = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESC));
                    dataItem.item_info_1 = cursor.getString(cursor.getColumnIndex(KEY_ITEM_INFO_1));

                    items.add(dataItem);
                }
            } finally {
                cursor.close();
            }
        }

        db.endTransaction();

        db.close();

        return items;
    }



    public ArrayList<DataItem> selectSimpleDataItemsByIds(SQLiteDatabase db, ArrayList<String> conditions) {

        ArrayList<DataItem> items = new ArrayList<>();

        StringBuilder conditionLike = new StringBuilder("");

        for (int i = 0; i < conditions.size(); i++) {
            String like = KEY_ITEM_ID + " LIKE '" + conditions.get(i) + "%' ";
            if (i != 0)  like = "OR " + like;
            conditionLike.append(like);
        }

        String query = "SELECT * FROM " +TABLE_ITEMS_DATA
                +" WHERE ("+conditionLike+") AND ("+KEY_ITEM_MODE+" < "+data_mode+")";

        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                items.add(getSimpleItemFromCursor(cursor));
            }

        } finally {
            cursor.close();
        }

        return items;
    }



    public Section selectSectionDataFromDB(SQLiteDatabase db, Section section) {

        int studiedDataCount;
        int knownDataCount;
        int allDataCount;
        int errorsCount;

        StringBuilder conditionLike = new StringBuilder("");
        StringBuilder conditionItemLike = new StringBuilder("WHERE ");


        for (int i = 0; i < section.catIds.size(); i++) {
            String like = KEY_USER_ITEM_ID + " LIKE '" + section.catIds.get(i) + "%' ";
            String itemLike = KEY_ITEM_ID + " LIKE '" + section.catIds.get(i) + "%' ";

            if (i != 0) {
                like = "OR " + like;
                itemLike = "OR " +  itemLike;
            }

            conditionLike.append(like);
            conditionItemLike.append(itemLike);
        }


        String dataItemsQuery = "SELECT * FROM " + TABLE_USER_DATA + " WHERE (" + conditionLike +") AND (" +
                KEY_ITEM_SCORE + " > 0 AND " + KEY_ITEM_SCORE + "< 3)";

        Cursor knownItemsCursor = db.rawQuery(dataItemsQuery , null);

        knownDataCount = knownItemsCursor.getCount();

        knownItemsCursor.close();


        dataItemsQuery = "SELECT * FROM " + TABLE_USER_DATA + " WHERE (" + conditionLike +") AND " +
                KEY_ITEM_SCORE + " > 2";

        Cursor studiedItemsCursor = db.rawQuery(dataItemsQuery , null);

        studiedDataCount = studiedItemsCursor.getCount();

        studiedItemsCursor.close();

        dataItemsQuery = "SELECT * FROM " + TABLE_USER_DATA + " WHERE (" + conditionLike +") AND " +
                KEY_ITEM_ERRORS + " > 0";

        Cursor errorsItemsCursor = db.rawQuery(dataItemsQuery , null);

        errorsCount = errorsItemsCursor.getCount();

        errorsItemsCursor.close();


        dataItemsQuery = "SELECT * FROM " + TABLE_ITEMS_DATA + " " + conditionItemLike;

        Cursor allItemsCursor = db.rawQuery(dataItemsQuery , null);
        allDataCount = allItemsCursor.getCount();
        allItemsCursor.close();

        section.studiedDataCount = studiedDataCount;
        section.knownDataCount = knownDataCount;
        section.allDataCount = allDataCount;
        section.errorsCount = errorsCount;


        return section;
    }


    public ArrayList<DataItem> getSectionListDataItems(ArrayList<NavCategory> navCategories) {
        ArrayList<DataItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();


        String query =  "SELECT * FROM "
                +TABLE_ITEMS_DATA +" a LEFT JOIN "+TABLE_USER_DATA
                +" b ON a.item_id=b.user_item_id"
                +" WHERE (a."+KEY_ITEM_ID + " LIKE ?) AND (a."+KEY_ITEM_MODE+" < "+data_mode+") ORDER BY a.id";

        NavCategory navCategory = new NavCategory();

        for (int i = 0; i < navCategories.size(); i++) {

            navCategory = navCategories.get(i);


            if (navCategory.review) {

                Cursor cursor = db.rawQuery(query, new String[]{navCategory.id + "%"});

                try {
                    while (cursor.moveToNext()) {

                        if (cursor.isFirst()) {

                            DataItem dataItem = new DataItem();
                            dataItem.type = "group_title";
                            dataItem.item = navCategory.title;
                            items.add(dataItem);
                        }

                        items.add(getItemFromCursor(cursor));

                    }
                } finally {
                    cursor.close();
                }
            }

        }


        db.close();

        return items;
    }



    public UserStats getAllDataItemsStats(UserStats stats) {  /// TODO fix it

        int studiedCount = 0;
        int knownCount = 0;
        int allCount = 0;


        SQLiteDatabase db = this.getWritableDatabase();


        StringBuilder conditionLike = new StringBuilder("");
        StringBuilder userConditionLike = new StringBuilder("");

        for (int i = 0; i < stats.userStatsData.idsToCheck.size(); i++) {
            String like = KEY_ITEM_ID + " LIKE '" + stats.userStatsData.idsToCheck.get(i) + "%' ";
            String userLike = KEY_USER_ITEM_ID + " LIKE '" + stats.userStatsData.idsToCheck.get(i) + "%' ";

            if (i != 0) {
                like = "OR " + like;
                userLike = "OR " + userLike;
            }
            conditionLike.append(like);
            userConditionLike.append(userLike);
        }


        String query = "SELECT * FROM " +TABLE_ITEMS_DATA
                +" WHERE ("+conditionLike +")"
                + "AND ("+KEY_ITEM_MODE+" < "+data_mode+")";

        Cursor cursor = db.rawQuery(query, null);

        allCount = cursor.getCount();

        query = "SELECT * FROM "
                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"

                +" WHERE ("+userConditionLike+") "
                +" AND (b."+KEY_ITEM_MODE+" < "+data_mode+") "

                +" AND (a."+KEY_ITEM_SCORE +" > 0) ";

        cursor = db.rawQuery(query, null);
        knownCount = cursor.getCount();

        query = "SELECT * FROM "

                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"
                +" WHERE ("+userConditionLike+") "
                +" AND (b."+KEY_ITEM_MODE+" < "+data_mode+") "

                +" AND (a."+KEY_ITEM_SCORE +" > 2) ";

        cursor = db.rawQuery(query, null);
        studiedCount = cursor.getCount();

        cursor.close();

        stats.userStatsData.allDataCount = allCount;
        stats.userStatsData.studiedDataCount = studiedCount;
        stats.userStatsData.familiarDataCount = knownCount;

        db.close();

        return stats;
    }



    public ArrayList<DataItem> getCatByTag(String cat) {  //// mode for cat

        ArrayList<DataItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String idPrefix = cat + "%";

        String query = "SELECT * FROM "
                +TABLE_ITEMS_DATA +" a LEFT JOIN "+TABLE_USER_DATA
                +" b ON a.item_id=b.user_item_id"
                +" WHERE (a.item_id LIKE ?) AND (a.item_mode < "+data_mode+") ORDER BY a.id";

        Cursor cursor = db.rawQuery(query, new String[]{idPrefix});

        try {
            while (cursor.moveToNext()) {
                items.add(getItemFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }

        db.close();
        return items;
    }



    public ArrayList<DataItem> getStarredFromDB(ArrayList<NavCategory> navCategories) {
        return getStarredFromDB(1, navCategories);
    }

    public ArrayList<DataItem> getStarredFromDB(int type, ArrayList<NavCategory> navCategories) {

        StringBuilder conditionLike = new StringBuilder("");

        for (int i = 0; i < navCategories.size(); i++) {

            String like = "a."+KEY_USER_ITEM_ID + " LIKE '" + navCategories.get(i).id + "%' ";

            if (i != 0) {
                like = "OR " + like;
            }
            conditionLike.append(like);
        }


        ArrayList<DataItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM "
                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"
                +" WHERE ("+conditionLike+") AND a."+KEY_ITEM_STARRED +" > ? ORDER BY b.id";

        Cursor cursor = db.rawQuery(query, new String[]{"0"});

        try {
            while (cursor.moveToNext()) {

                DataItem item = getItemFromCursor(cursor);
                if (item.db_filter == null) item.db_filter = "";
                if (type == 2) {
                    if (item.db_filter.contains(GALLERY_TAG)) items.add(item);
                } else {
                    if (!item.db_filter.contains(GALLERY_TAG)) items.add(item);
                }
            }
        } finally {
            cursor.close();
        }

        return items;
    }


    public DataItem getDataItemById(String detail_id) {

        DataItem dataItem = new DataItem();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS_DATA,  null, KEY_ITEM_ID +" = ?",
                new String[] { detail_id}, null, null, null);

        if ( cursor.moveToFirst() ) {
            dataItem = getSimpleItemFromCursor(cursor);
        } else {
            dataItem.item = "not found";
            dataItem.info = "This entry doesn't exist";
        }

        cursor.close();
        db.close();

        return  dataItem;
    }




    public DetailItem getDetailById(String detail_id) {

        DetailItem detailItem = new DetailItem();

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.query(TABLE_DETAILS_DATA,  null, KEY_DETAIL_ID +" = ?",
                new String[] { detail_id}, null, null, null);

        if ( cursor.moveToFirst() ) {
            detailItem = getDetailFromCursor(cursor);
        } else {
            detailItem.title = "not found";
        }

        cursor.close();
        db.close();

        return  detailItem;
    }


    private DetailItem getDetailFromCursor(Cursor cursor) {
        DetailItem detailItem = new DetailItem();
        detailItem.id = cursor.getString(cursor.getColumnIndex(KEY_DETAIL_ID));
        detailItem.title = cursor.getString(cursor.getColumnIndex(KEY_DETAIL_TITLE));
        detailItem.desc = cursor.getString(cursor.getColumnIndex(KEY_DETAIL_DESC));
        detailItem.image = cursor.getString(cursor.getColumnIndex(KEY_DETAIL_IMAGE));
        detailItem.img_info = cursor.getString(cursor.getColumnIndex(KEY_DETAIL_IMG_INFO));

        return detailItem;
    }


    public Map<String, String> checkCatProgressDB(ArrayList<String> catIds ) {

        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder conditions = new StringBuilder("WHERE ");
        for (int i = 0; i < catIds.size(); i++) {
            String condition = KEY_CAT_ID+ " = '" + catIds.get(i) + "' ";
            if (i != 0) condition = "OR " + condition;
            conditions.append(condition);
        }

        String query = "SELECT * FROM " + TABLE_CAT_DATA + " " + conditions;

        Cursor cursor = db.rawQuery(query, null);

        Map<String,String> myMap = new HashMap<>();

        try {
            while (cursor.moveToNext()) {
                myMap.put(cursor.getString(cursor.getColumnIndex(KEY_CAT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_CAT_PROGRESS)));

            }
        } finally {
            cursor.close();
        }

        db.close();


        // Toast.makeText(cntx, "" + res, Toast.LENGTH_LONG).show();

        return myMap;

    }




    public UserStatsData getErrorsData(UserStatsData userStatsData) {  /// TODO add cat selection

        SQLiteDatabase db = this.getWritableDatabase();

        userStatsData.errorsWords = new ArrayList<>();
        userStatsData.mostErrorsWords = new ArrayList<>();

        String query = "SELECT * FROM "
                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"
                +" WHERE (a."+KEY_ITEM_ERRORS +" > ?) "
                // + " AND (b."+KEY_ITEM_MODE+" < "+data_mode+")"

                +"ORDER BY a."+KEY_ITEM_TIME_ERROR+" DESC LIMIT 30";

        Cursor errorsCursor = db.rawQuery(query, new String[]{"0"});


        try {
            while (errorsCursor.moveToNext()) {
                userStatsData.errorsWords.add(getItemFromCursor(errorsCursor));
            }
        } finally {
            errorsCursor.close();
        }

        String mostQuery = "SELECT * FROM "
                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"
                +" WHERE (a."+KEY_ITEM_ERRORS +" > ?) "
                // + " AND (b."+KEY_ITEM_MODE+" < "+data_mode+")"


                +"ORDER BY a."+KEY_ITEM_TIME_ERROR+" DESC, a."+ KEY_ITEM_ERRORS +" DESC LIMIT 30";


        Cursor mostErrorsCursor = db.rawQuery(mostQuery, new String[]{"0"});

        try {
            while (mostErrorsCursor.moveToNext()) {

                userStatsData.mostErrorsWords.add(getItemFromCursor(mostErrorsCursor));
            }
        } finally {
            mostErrorsCursor.close();
        }

        db.close();


        return userStatsData;
    }


    public ArrayList<DataItem> getOldestFromDB(ArrayList<String> checkCatIds) {

        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<DataItem> oldestData = getOldestData(db, checkCatIds);

        db.close();

        return oldestData;
    }



    private ArrayList<DataItem> getOldestData(SQLiteDatabase db, ArrayList<String> checkCatIds) {

        ArrayList<DataItem> items = new ArrayList<>();

        StringBuilder conditionLike = new StringBuilder("");


        for (int i = 0; i < checkCatIds.size(); i++) {
            String like = "a."+KEY_USER_ITEM_ID + " LIKE '" + checkCatIds.get(i)+ "%' ";

            if (i != 0) {
                like = "OR " + like;
            }
            conditionLike.append(like);
        }


        String query = "SELECT * FROM "
                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"
                +" WHERE ("+conditionLike+") AND a."+KEY_ITEM_SCORE +" > ? "
                +"ORDER BY a."+ KEY_ITEM_TIME +" ASC LIMIT "+String.valueOf(Constants.REVISE_NUM);

        Cursor cursor = db.rawQuery(query, new String[] { "0"});

        try {
            while (cursor.moveToNext()) {
                items.add(getItemFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }

        return  items;
    }



    public UserStatsData checkAppStatsDB(UserStatsData userStatsData) {

        SQLiteDatabase db = this.getWritableDatabase();

        userStatsData.sectionsDataList = checkSectionListStatsDB(db, userStatsData.sectionsDataList);

        db.close();
        return userStatsData;
    }




    Section checkSectionStatsDB(Section section) {

        SQLiteDatabase db = this.getWritableDatabase();
        section = checkSectionStatsDB(db, section);
        db.close();

        return section;
    }


    private ArrayList<Section> checkSectionListStatsDB(SQLiteDatabase db, ArrayList<Section> sections) {
        for (Section section: sections) {
            section = checkSectionStatsDB(db, section);
        }
        return sections;
    }


    private Section checkSectionStatsDB(SQLiteDatabase db, Section section) {  //// TODO results for section

        section = getSectionTestsResults(db, section);
        section = checkSectionDataStatus(db, section);

        section.calculateProgress();

        return section;
    }


    private Section getSectionTestsResults(SQLiteDatabase db, Section section) {

        int testsResults = 0;
        section.stadiedCatsCount = 0;

        StringBuilder conditions = new StringBuilder("WHERE ");

        for (int i = 0; i < section.checkCatIds.size(); i++) {
            String condition = KEY_CAT_ID+ " = '" + section.checkCatIds.get(i) + "' ";
            if (i != 0) condition = "OR " + condition;
            conditions.append(condition);
        }

        //// getting section tests results

        String query = "SELECT * FROM " + TABLE_CAT_DATA + " " + conditions;

        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                int result = Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_CAT_PROGRESS)));
                testsResults += result;
                if (result > 50) section.stadiedCatsCount ++;
            }
        } finally {
            cursor.close();
        }

        section.testResults = testsResults / section.checkCatIds.size() ;


        query = "SELECT * FROM " + TABLE_TESTS_DATA + " WHERE "+KEY_TEST_TAG
                +" LIKE '"+Constants.SECTION_TEST_PREFIX + section.id + "%'";

        Cursor tcursor = db.rawQuery(query, null);

        int allResult = 0;

        Map<String,String> myMap = new HashMap<>();

        try {
            while (tcursor.moveToNext()) {
                int result = Integer.valueOf(tcursor.getString(tcursor.getColumnIndex(KEY_TEST_PROGRESS)));


                myMap.put(
                        tcursor.getString(tcursor.getColumnIndex(KEY_TEST_TAG)),
                        tcursor.getString(tcursor.getColumnIndex(KEY_TEST_PROGRESS)) );


                allResult += result;
            }
        } finally {
            tcursor.close();
        }

        section.controlMap = myMap;
        section.controlTests = allResult / Constants.SECTION_TESTS_NUM;


        return section;
    }



    private Section checkSectionDataStatus(SQLiteDatabase db, Section section) {

        int studiedDataCount = 0;
        int knownDataCount = 0;
        int allDataCount = 0;

        int errorsCount = 0;

        StringBuilder conditionLike = new StringBuilder("");
        StringBuilder conditionItemLike = new StringBuilder("");


        for (int i = 0; i < section.catIds.size(); i++) {
            String like = "a."+KEY_USER_ITEM_ID + " LIKE '" + section.catIds.get(i) + "%' ";
            String itemLike = KEY_ITEM_ID + " LIKE '" + section.catIds.get(i) + "%' ";
            if (i != 0) {
                like = "OR " + like;
                itemLike = "OR " +  itemLike;
            }
            conditionLike.append(like);
            conditionItemLike.append(itemLike);
        }

        StringBuilder conditionAllCatLike = new StringBuilder("");

        for (int i = 0; i < section.allCatIds.size(); i++) {
            String like = "a."+KEY_USER_ITEM_ID + " LIKE '" + section.allCatIds.get(i) + "%' ";
            if (i != 0)  like = "OR " + like;
            conditionAllCatLike.append(like);
        }

                        //TODO CHECK INNER
        String dataItemsQuery = "SELECT * FROM "

                +TABLE_USER_DATA +" a INNER JOIN "+ TABLE_ITEMS_DATA +" b ON a.user_item_id=b.item_id"

                + " WHERE (" + conditionLike +") "

                + "AND (a." + KEY_ITEM_SCORE + " > 0 AND a." + KEY_ITEM_SCORE + "< 3) "

                + "AND (b."+KEY_ITEM_MODE+" < "+data_mode+")";

        Cursor knownItemsCursor = db.rawQuery(dataItemsQuery , null);

        knownDataCount = knownItemsCursor.getCount();

        knownItemsCursor.close();


        dataItemsQuery = "SELECT * FROM "

                + TABLE_USER_DATA +" a INNER JOIN "+ TABLE_ITEMS_DATA +" b ON a.user_item_id=b.item_id"
                + " WHERE (" + conditionLike +") "

                + "AND (a." + KEY_ITEM_SCORE + " > 2) "

                + "AND (b."+KEY_ITEM_MODE+" < "+data_mode+")";

        Cursor studiedItemsCursor = db.rawQuery(dataItemsQuery , null);

        studiedDataCount = studiedItemsCursor.getCount();

        studiedItemsCursor.close();


        dataItemsQuery = "SELECT * FROM "       /// TODO check mode for errors

                + TABLE_USER_DATA +" a INNER JOIN "+ TABLE_ITEMS_DATA +" b ON a.user_item_id=b.item_id"

                + " WHERE (" + conditionAllCatLike +")"
                // + "AND (b."+KEY_ITEM_MODE+" < "+data_mode+")"

                + "AND (a." + KEY_ITEM_ERRORS + " > 0)";

        Cursor errorsItemsCursor = db.rawQuery(dataItemsQuery , null);

        errorsCount = errorsItemsCursor.getCount();

        errorsItemsCursor.close();


        dataItemsQuery = "SELECT * FROM " + TABLE_ITEMS_DATA
                + " WHERE  (" + conditionItemLike +")"
                +" AND ("+KEY_ITEM_MODE+" < "+data_mode+")";

        Cursor allItemsCursor = db.rawQuery(dataItemsQuery , null);
        allDataCount = allItemsCursor.getCount();
        allItemsCursor.close();

        section.studiedDataCount = studiedDataCount;
        section.knownDataCount = knownDataCount;
        section.allDataCount = allDataCount;
        section.errorsCount = errorsCount;


        return section;
    }


    public Section getSectionCatItemsStats(Section section) {

        SQLiteDatabase db = this.getWritableDatabase();

        section = checkSectionDataStatus(db, section);

        section = getSectionCatItemsStatsDB(db, section);

        section.calculateProgress();

        db.close();

        return section;
    }



    private Section getSectionCatItemsStatsDB(SQLiteDatabase db, Section section) {

        for (Category category: section.categories) {

            String idPrefix = category.id+"%";

            String query = "SELECT * FROM "
                    +TABLE_ITEMS_DATA +" a LEFT JOIN "+TABLE_USER_DATA
                    +" b ON a.item_id=b.user_item_id"

                    +" WHERE (a."+KEY_ITEM_ID+" LIKE ?) "
                    +" AND(a."+KEY_ITEM_MODE+" < "+data_mode+") "

                    +" ORDER BY a.id";


            Cursor cursor = db.rawQuery(query , new String[]{idPrefix});

            category.allDataCount = cursor.getCount();


            query = "SELECT * FROM "

                    +TABLE_ITEMS_DATA +" a LEFT JOIN "+TABLE_USER_DATA +" b ON a.item_id=b.user_item_id"

                    +" WHERE (a." + KEY_ITEM_ID + " LIKE ?) "
                    +" AND(a."+KEY_ITEM_MODE+" < "+data_mode+") "

                    +" AND (b."+KEY_ITEM_SCORE +" >0 AND b."+KEY_ITEM_SCORE +" <3)";

            cursor = db.rawQuery(query , new String[]{idPrefix});

            category.knownDataCount = cursor.getCount();


            query = "SELECT * FROM "

                    +TABLE_ITEMS_DATA +" a LEFT JOIN "+TABLE_USER_DATA +" b ON a.item_id=b.user_item_id"
                    +" WHERE (a." + KEY_ITEM_ID + " LIKE ?) "
                    +" AND(a."+KEY_ITEM_MODE+" < "+data_mode+") "

                    +"AND (b."+KEY_ITEM_SCORE +" > 2)";


            cursor = db.rawQuery(query , new String[]{idPrefix});

            category.studiedDataCount = cursor.getCount();


            category.calculateDataCount();


            cursor.close();

        }



        return section;
    }





    public ArrayList<Section> checkSectionsStats(ArrayList<Section> sectionsDataList) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (Section section: sectionsDataList) {

            String idPrefix = section.id+"%";
            int testResult = 0;
            String testId = section.id + "_1";


            Cursor cursor = db.query(TABLE_USER_DATA, new String[] {KEY_USER_ITEM_ID, KEY_ITEM_SCORE},
                    KEY_USER_ITEM_ID + " LIKE ? AND " + KEY_ITEM_SCORE + " > ? AND "+ KEY_ITEM_SCORE + " < ?",
                    new String[]{idPrefix, "0", "3"}, null, null, null);

            section.knownDataCount = cursor.getCount();


            cursor = db.query(TABLE_USER_DATA, new String[] {KEY_USER_ITEM_ID, KEY_ITEM_SCORE},
                    KEY_USER_ITEM_ID + " LIKE ? AND " + KEY_ITEM_SCORE + " > ? ",
                    new String[]{idPrefix, "2"}, null, null, null);

            section.studiedDataCount = cursor.getCount();


            cursor = db.query(TABLE_ITEMS_DATA, new String[] {KEY_ITEM_ID},
                    KEY_ITEM_ID + " LIKE ?",
                    new String[]{idPrefix}, null, null, null);

            section.allDataCount = cursor.getCount();


            Cursor testCursor = db.query(TABLE_TESTS_DATA, new String[] { KEY_TEST_TAG, KEY_TEST_PROGRESS },
                    KEY_TEST_TAG + " = ?", new String[]{ testId }, null, null, null);


            if ( testCursor.moveToFirst() ) {
                testResult = testCursor.getInt(testCursor.getColumnIndex(KEY_TEST_PROGRESS));
            }

            section.testResults = testResult;

            section.calculateProgress();

            testCursor.close();
            cursor.close();
        }


        return sectionsDataList;
    }



    public ArrayList<DataItem> getSectionErrorsData(Section section) { // TODO check order

        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<DataItem> errors = new ArrayList<>();

        StringBuilder conditionLike = new StringBuilder("");


        for (int i = 0; i < section.allCatIds.size(); i++) {
            String like = "a."+KEY_USER_ITEM_ID + " LIKE '" + section.allCatIds.get(i) + "%' ";

            if (i != 0) {
                like = "OR " + like;
            }
            conditionLike.append(like);
        }


        String mostQuery = "SELECT * FROM "
                +TABLE_USER_DATA +" a INNER JOIN "+TABLE_ITEMS_DATA
                +" b ON a.user_item_id=b.item_id"
                +" WHERE ("+conditionLike +") "
                // +" AND (b." + KEY_ITEM_MODE + " < " + data_mode + ") "

                +"AND (a."+KEY_ITEM_ERRORS +" > ?) "
                +"ORDER BY a."+KEY_ITEM_TIME_ERROR+" DESC, a."+ KEY_ITEM_ERRORS +" DESC LIMIT 30";


        Cursor mostErrorsCursor = db.rawQuery(mostQuery, new String[]{"0"});

        try {
            while (mostErrorsCursor.moveToNext()) {
                errors.add(getItemFromCursor(mostErrorsCursor));
            }
        } finally {
            mostErrorsCursor.close();
        }

        db.close();


        return errors;
    }


    private DataItem getSimpleItemFromCursor(Cursor cursor) {
        DataItem dataItem = new DataItem();

        dataItem.id = cursor.getString(cursor.getColumnIndex(KEY_ITEM_ID));
        dataItem.item = cursor.getString(cursor.getColumnIndex(KEY_ITEM_TITLE));
        dataItem.info = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESC));
        dataItem.image = cursor.getString(cursor.getColumnIndex(KEY_ITEM_IMAGE));
        dataItem.item_info_1 = cursor.getString(cursor.getColumnIndex(KEY_ITEM_INFO_1));
        dataItem.divider = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DIVIDER));
        dataItem.filter = cursor.getString(cursor.getColumnIndex(KEY_ITEM_FILTER));

        return dataItem;
    }


    private DataItem getItemFromCursor(Cursor cursor) {

        DataItem dataItem = getSimpleItemFromCursor(cursor);

        dataItem.starred = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_STARRED));
        dataItem.rate = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SCORE));
        dataItem.errors = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ERRORS));

        dataItem.db_filter = cursor.getString(cursor.getColumnIndex(KEY_ITEM_INFO));

        dataItem.starred_time = cursor.getLong(cursor.getColumnIndex(KEY_ITEM_TIME_STARRED));
        dataItem.time = cursor.getLong(cursor.getColumnIndex(KEY_ITEM_TIME));
        dataItem.time_errors = cursor.getLong(cursor.getColumnIndex(KEY_ITEM_TIME_ERROR));

        return dataItem;
    }



    public ArrayList<DataItem> checkStarredList(ArrayList<DataItem> words) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (DataItem word: words) {
            Cursor cursor = db.query(TABLE_USER_DATA, null, KEY_USER_ITEM_ID + " = ? ",
                    new String[]{word.id}, null, null, null);

            if (cursor.moveToFirst()) {
                cursor.moveToFirst();

                word.starred = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_STARRED));
                word.starred_time = cursor.getLong(cursor.getColumnIndex(KEY_ITEM_TIME_STARRED));
                word.rate = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SCORE));
                word.errors = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ERRORS));

            }
            cursor.close();
        }
        db.close();

        return words;
    }



    public void createNote(NoteData note) {

        SQLiteDatabase db = this.getWritableDatabase();

        createNote(db, note);

        db.close();
    }

    public void createNote(SQLiteDatabase db, NoteData note) {

        long time = System.currentTimeMillis();

        ContentValues values = new ContentValues();

        values.put(KEY_NOTE_TITLE, note.title);
        values.put(KEY_NOTE_TEXT, note.content);
        values.put(KEY_NOTE_ICON, note.image);
        values.put(KEY_NOTE_STATUS, note.status);
        values.put(KEY_NOTE_PARAMS, note.params);
        values.put(KEY_NOTE_CREATED, time );
        values.put(KEY_NOTE_UPDATED, time );
        values.put(KEY_NOTE_UPDATED_SORT, time );

        long primary_id = db.insert(TABLE_NOTES_DATA, null, values);

        Cursor cursor = db.query(TABLE_NOTES_DATA,  null,
                KEY_NOTE_PRIMARY_ID +" = ? " ,

                new String[] { String.valueOf(primary_id)  }, null, null, null);



        if (cursor.moveToNext()) {

            ContentValues newValues = new ContentValues();

            newValues.put(KEY_NOTE_ID, "note_" + primary_id);

            db.update(TABLE_NOTES_DATA, newValues,
                    KEY_NOTE_PRIMARY_ID +" = ? ",
                    new String[] { String.valueOf(primary_id) });

        }

        cursor.close();

    }




    public void updateNote(NoteData note) {

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.query(TABLE_NOTES_DATA,  null,
                KEY_NOTE_ID +" = ?",
                new String[] { note.id}, null, null, null);


        if (cursor.moveToFirst() ) {

            // Toast.makeText(cntx, "FOUND", Toast.LENGTH_SHORT).show();

            ContentValues values = new ContentValues();

            values.put(KEY_NOTE_TITLE, note.title);
            values.put(KEY_NOTE_TEXT, note.content);
            values.put(KEY_NOTE_ICON, note.image);
            values.put(KEY_NOTE_UPDATED, System.currentTimeMillis());

            db.update(TABLE_NOTES_DATA, values, KEY_NOTE_ID +" = ?", new String[] { note.id });
        } else {
            // Toast.makeText(cntx, "NOT FOUND: "+ note.id, Toast.LENGTH_SHORT).show();
        }

        cursor.close();


        db.close();
    }



    public void deleteNote(NoteData note) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NOTES_DATA,  null,
                KEY_NOTE_ID +" = ?",
                new String[] { note.id}, null, null, null);


        if (cursor.moveToFirst() ) {
            db.delete(TABLE_NOTES_DATA, KEY_NOTE_ID + " = ?", new String[]{note.id});
        }

        cursor.close();

        db.close();

    }


    public ArrayList<NoteData> getNotes() {

        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<NoteData> items = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NOTES_DATA,  null, null, null,
                null, null, KEY_NOTE_UPDATED_SORT + " DESC");

        try {
            while (cursor.moveToNext()) {

                NoteData note  = getNoteFromCursor(cursor);

                if (!note.parent.contains(NOTE_ARCHIVE))

                    items.add(note);
            }

        } finally {
            cursor.close();
        }

        db.close();

        return  items;
    }

    public NoteData getNote(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        NoteData note = new NoteData();
        note.title = "";
        note.status = "not_found";

        //Toast.makeText(cntx, "ID: " + id, Toast.LENGTH_SHORT).show();

        Cursor cursor = db.query(TABLE_NOTES_DATA,  null,
                KEY_NOTE_ID +" = ?",
                new String[] { id}, null, null, null);


        if (cursor.moveToFirst() ) {
            note.status = "";
            note  = getNoteFromCursor(cursor);

        }

        cursor.close();

        db.close();

        return note;
    }

    private NoteData getNoteFromCursor(Cursor cursor) {

        NoteData note  = new NoteData();

        note.id = cursor.getString(cursor.getColumnIndex(KEY_NOTE_ID));
        note.title = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
        note.content = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TEXT));
        note.image = cursor.getString(cursor.getColumnIndex(KEY_NOTE_ICON));

        note.time_updated = cursor.getLong(cursor.getColumnIndex(KEY_NOTE_UPDATED));
        note.time_created = cursor.getLong(cursor.getColumnIndex(KEY_NOTE_CREATED));
        note.time_updated_sort = cursor.getLong(cursor.getColumnIndex(KEY_NOTE_UPDATED_SORT));

        note.parent = cursor.getString(cursor.getColumnIndex(KEY_NOTE_PARENT));

        if (note.parent == null) note.parent = "";

        return note;
    }



    public void updateNoteSortTime(NoteData dataObject) {

        SQLiteDatabase db = this.getWritableDatabase();

        dataObject.time_updated = System.currentTimeMillis();

        ContentValues values = new ContentValues();

        values.put(KEY_NOTE_UPDATED_SORT, dataObject.time_updated );

        Cursor cursor = db.query(TABLE_NOTES_DATA,  null,
                KEY_NOTE_ID +" = ?",
                new String[] { dataObject.id }, null, null, null);

        if (cursor.moveToFirst() ) {

            db.update(TABLE_NOTES_DATA, values,
                    KEY_NOTE_ID +" = ?",
                    new String[] { dataObject.id });

            //Toast.makeText(cntx, "Time: " + dataObject.time_updated, Toast.LENGTH_SHORT).show();
        }



        cursor.close();
        db.close();

    }

    public void parentNote(String noteId, String parent) {

        SQLiteDatabase db = this.getWritableDatabase();

        long time = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTE_PARENT, parent);
        // values.put(KEY_NOTE_UPDATED, dataObject.time_updated);
        values.put(KEY_NOTE_UPDATED_SORT, time );


        int n = db.update(TABLE_NOTES_DATA, values,
                KEY_NOTE_ID +" = ?",
                new String[] { noteId });


        //Toast.makeText(cntx, "N " + n, Toast.LENGTH_SHORT).show();

        db.close();


    }


    public ArrayList<NoteData> getNotesListForSet(String set_id) {


        ArrayList<NoteData> notes = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NOTES_DATA,  null, KEY_NOTE_PARENT +" = ?", new String[]{set_id},
                null, null, KEY_NOTE_UPDATED_SORT + " DESC");

        try {
            while (cursor.moveToNext()) {

                NoteData note = getNoteFromCursor(cursor);

                notes.add(note);
            }

        } finally {
            cursor.close();
        }

        db.close();

        return notes;
    }





    public void sanitizeDB() {

        SQLiteDatabase db = this.getWritableDatabase();

        sanitizeDB(db);

        db.close();
    }



    private void sanitizeDB(SQLiteDatabase db) {

        DataFromJson dataFromJson= new DataFromJson(cntx);
        ArrayList<DataItem> allItems = dataFromJson.getItemsFromAllFiles();

        Cursor cursor = db.query(TABLE_USER_DATA, new String[]{KEY_USER_ITEM_ID}, null, null, null, null, null);

        int del = 0;


        try {
            while (cursor.moveToNext()) {
                String txt = cursor.getString(cursor.getColumnIndex(KEY_USER_ITEM_ID));
                boolean found = false;

                DataItem foundW = new DataItem();
                for (DataItem word: allItems) {
                    if (word.id.equals(txt)) {
                        foundW = word;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    allItems.remove(foundW);
                } else {
                    db.delete(TABLE_USER_DATA, KEY_USER_ITEM_ID +" = ?", new String[]{txt});
                    del++;
                }
            }
        } finally {
            cursor.close();
        }
    }



    public int deleteExData(String[] cat_tag) {
        int count = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        for (String aCat_tag : cat_tag) {
            int t = db.delete(TABLE_TESTS_DATA, KEY_TEST_TAG+" = ?", new String[]{aCat_tag});
            count = count + t;

        }
        db.close();

        return count;
    }


    private int countScore(int currentScore, int result) {
        int score = currentScore + result;
        if (score < 0) score = 0;
        if (score > MAX_SCORE) score = MAX_SCORE;
        return score;
    }


    public void importUserData(SQLiteDatabase db, List<DBImport.UserItemData> list) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        db.execSQL(CREATE_USER_ITEMS_TABLE_IF_EXISTS);

        db.beginTransaction();

        try {

            for (DBImport.UserItemData item: list) {
                insertImportedUserItem(db, item);
            }

            db.setTransactionSuccessful();
            Toast.makeText(cntx, String.format(cntx.getString(R.string.entries_updated), list.size()), Toast.LENGTH_SHORT).show();

        } finally {
            db.endTransaction();
        }

    }


    private void insertImportedUserItem(SQLiteDatabase db, DBImport.UserItemData userItem) {

        /// TODO check for type if needed

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ITEM_ID, userItem.id);
        values.put(KEY_ITEM_INFO, userItem.itemInfo);
        values.put(KEY_ITEM_PROGRESS, userItem.itemProgress);
        values.put(KEY_ITEM_ERRORS, userItem.itemErrors);
        values.put(KEY_ITEM_SCORE, userItem.itemScore);
        values.put(KEY_ITEM_STATUS, userItem.itemStatus);
        values.put(KEY_ITEM_STARRED, userItem.itemStarred);
        values.put(KEY_ITEM_TIME, userItem.itemTime);
        values.put(KEY_ITEM_TIME_STARRED, userItem.itemTimeStarred);
        values.put(KEY_ITEM_TIME_ERROR, userItem.itemTimeError);

        db.insert(TABLE_USER_DATA, null, values);
    }


    public void importCatData (SQLiteDatabase db, List<DBImport.CatDataLine> list) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT_DATA);
        db.execSQL(CREATE_CATDATA_TABLE);

        db.beginTransaction();

        try {

            for (DBImport.CatDataLine item: list) {
                insertImportedCatData(db, item);
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

    }


    private void insertImportedCatData(SQLiteDatabase db, DBImport.CatDataLine catData) {

        /// TODO check for type if needed

        ContentValues values = new ContentValues();
        values.put(KEY_CAT_ID, catData.catId);
        values.put(KEY_CAT_PROGRESS, catData.progress);

        db.insert(TABLE_CAT_DATA, null, values);
    }


    public void importTestsData (SQLiteDatabase db, List<DBImport.TestData> list) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS_DATA);
        db.execSQL(CREATE_TESTS_TABLE);

        db.beginTransaction();

        try {

            for (DBImport.TestData item: list) {
                insertImportedTestsData(db, item);
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

    }


    private void insertImportedTestsData(SQLiteDatabase db, DBImport.TestData testData) {

        /// TODO check for type if needed

        ContentValues values = new ContentValues();
        values.put(KEY_TEST_TAG, testData.tag);
        values.put(KEY_TEST_PROGRESS, testData.progress);
        values.put(KEY_TEST_TIME, testData.testTime);

        db.insert(TABLE_TESTS_DATA, null, values);
    }

    public void importNotesData (SQLiteDatabase db, List<DBImport.NoteDataDB> list) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_DATA);
        db.execSQL(CREATE_NOTES_TABLE);

        db.beginTransaction();

        try {

            for (DBImport.NoteDataDB item: list) {
                insertImportedNoteData(db, item);
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

    }

    private void insertImportedNoteData(SQLiteDatabase db, DBImport.NoteDataDB noteData) {

        ContentValues values = new ContentValues();

        values.put(KEY_NOTE_PRIMARY_ID, noteData.notePrimaryKey);
        values.put(KEY_NOTE_ID, noteData.noteId);
        values.put(KEY_NOTE_TITLE, noteData.noteTitle);
        values.put(KEY_NOTE_TEXT, noteData.noteContent);
        values.put(KEY_NOTE_ICON, noteData.noteIcon);
        values.put(KEY_NOTE_INFO, noteData.noteInfo);
        values.put(KEY_NOTE_STATUS, noteData.noteStatus);
        values.put(KEY_NOTE_TYPE, noteData.noteType);
        values.put(KEY_NOTE_PARAMS, noteData.noteParams);
        values.put(KEY_NOTE_FILTER, noteData.noteFilter);
        values.put(KEY_NOTE_PARENT, noteData.noteParent);
        values.put(KEY_NOTE_ORDER, noteData.noteOrder);
        values.put(KEY_NOTE_CREATED, noteData.noteCreated);
        values.put(KEY_NOTE_UPDATED, noteData.noteUpdated);
        values.put(KEY_NOTE_UPDATED_SORT, noteData.noteUpdatedSort);

        db.insert(TABLE_NOTES_DATA, null, values);
    }





}
