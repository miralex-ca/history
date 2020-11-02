package com.online.languages.study.studymaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.online.languages.study.studymaster.adapters.ImgPickerAdapter;
import com.online.languages.study.studymaster.adapters.InfoDialog;
import com.online.languages.study.studymaster.adapters.OpenActivity;
import com.online.languages.study.studymaster.adapters.RoundedCornersTransformation;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.DataManager;
import com.online.languages.study.studymaster.data.NoteData;
import com.squareup.picasso.Picasso;

import static com.online.languages.study.studymaster.Constants.ACTION_CREATE;
import static com.online.languages.study.studymaster.Constants.ACTION_UPDATE;
import static com.online.languages.study.studymaster.Constants.EXTRA_NOTE_ACTION;
import static com.online.languages.study.studymaster.Constants.EXTRA_NOTE_ID;
import static com.online.languages.study.studymaster.Constants.FOLDER_PICS;
import static com.online.languages.study.studymaster.Constants.NOTE_PIC_DEFAULT_INDEX;


public class NoteEditActivity extends BaseActivity {


    ThemeAdapter themeAdapter;
    SharedPreferences appSettings;
    public String themeTitle;

    OpenActivity openActivity;
    TextView title, content;
    ImageView noteIcon;

    private TextView titleCharCounter;
    private TextView contentCharCounter;

    private EditText titleEditText;
    private EditText contentEditText;

    int titleCharMax = 60;
    int contentCharMax = 5000;
    ImgPickerAdapter imgPickerAdapter;
    RecyclerView recyclerView;

    int picIndex = NOTE_PIC_DEFAULT_INDEX ;
    String [] pics = new String[] {};
    String folder = "";

    String noteId;
    String noteAction;

    DataManager dataManager;
    NoteData note;

    AlertDialog imgPickerDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        themeTitle= appSettings.getString("theme", Constants.SET_THEME_DEFAULT);

        themeAdapter = new ThemeAdapter(this, themeTitle, false);
        themeAdapter.getTheme();

        openActivity = new OpenActivity(this);
        openActivity.setOrientation();

        setContentView(R.layout.activity_note_edit);


        noteAction = getIntent().getStringExtra(EXTRA_NOTE_ACTION);
        noteId = getIntent().getStringExtra(EXTRA_NOTE_ID);

        dataManager = new DataManager(this);

        pics = getResources().getStringArray(R.array.note_pics_list);

        folder = getString(R.string.notes_pics_folder);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_toolbar);

        setTitle("");

        titleCharCounter = findViewById(R.id.titleCharCounter);
        contentCharCounter = findViewById(R.id.contentCharCounter);


        titleCharCounter.setText("0/"+titleCharMax);
        contentCharCounter.setText("0/"+ contentCharMax);

        titleEditText = findViewById(R.id.editTitle);
        contentEditText = findViewById(R.id.editContent);

        titleEditText.addTextChangedListener(titleEditorWatcher);
        contentEditText.addTextChangedListener(contentEditorWatcher);


        noteIcon = findViewById(R.id.editNoteIcon);

        setIconImage(pics[picIndex]);

        if (noteAction.equals(ACTION_UPDATE)) getNote();



    }


    private void getNote() {

        note = dataManager.dbHelper.getNote(noteId);

        titleEditText.setText(note.title);
        contentEditText.setText(note.content);

        setIconImage(note.image);

        picIndex = getIconIndex(note.image);

    }



    private void setIconImage(String pic) {

        Picasso.with( this )
                .load(FOLDER_PICS+ folder + pic )
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(20,0))
                .into(noteIcon);

    }


    private int getIconIndex(String pic) {

        int index = -1;

        for (int i = 0; i < pics.length; i ++) {
            if (pics[i].equals(pic)) index = i;
        }
        return index;
    }



    private String textSanitizer(String text) {

        text = text.replace("\n", " ").replace("\r", " ");

        text = text.trim();
        return text;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.save_note:
                editNote();
                return true;

            case R.id.info_item:
                showInfo();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfo() {
        InfoDialog infoDialog = new InfoDialog(this);
        infoDialog.simpleDialog(getString(R.string.info_notes_title), getString(R.string.info_notes_text));

    }

    private void editNote() {

        NoteData note = new NoteData();


        note.id = noteId;

        String title = titleEditText.getText().toString();
        note.title = textSanitizer(title);
        note.content = contentEditText.getText().toString();

        if (picIndex == -1 ) {
            note.image = pics[0];
        } else {
            note.image = pics[picIndex];
        }


        if (note.title.equals("") && note.content.trim().equals("")) {

            InfoDialog infoDialog = new InfoDialog(this);

            infoDialog.simpleDialog(getString(R.string.savaing_note_title), getString(R.string.empty_note_msg));

            return;
        }



        if (noteAction.equals(ACTION_UPDATE)) {
            dataManager.dbHelper.updateNote(note);
            //Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }

        if (noteAction.equals(ACTION_CREATE)) dataManager.dbHelper.createNote(note);


        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_edit, menu);

        return true;
    }



    private final TextWatcher titleEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String str = s.length() + "/" + titleCharMax;
            titleCharCounter.setText(str);
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher contentEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = s.length() + "/" + contentCharMax;
            contentCharCounter.setText(str);
        }

        public void afterTextChanged(Editable s) {
        }
    };



    public void buildDialog(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this );

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View content = inflater.inflate(R.layout.picker_dialog, null);


        recyclerView = content.findViewById(R.id.recycler_view);



        int spanCount = getResources().getInteger(R.integer.img_picker_span);

        imgPickerAdapter = new ImgPickerAdapter(this, pics, picIndex);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, spanCount);

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(imgPickerAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        dialog.setView(content);
        dialog.setCancelable(true);

        dialog.setNegativeButton(R.string.cancel_txt,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        /*

        dialog.setPositiveButton(R.string.apply_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setIconImage(pics[picIndex]);
                        dialog.cancel();
                    }
                });

         */



        imgPickerDialog = dialog.create();

        imgPickerDialog.show();



    }

    private static int dpToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public void checkPic(View view) {

        View icon = view.findViewById(R.id.icon);
        int t = (int) icon.getTag();
        picIndex = t;
        imgPickerAdapter = new ImgPickerAdapter(this, pics, t);
        recyclerView.setAdapter(imgPickerAdapter);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                imgPickerDialog.dismiss();

            }
        }, 40);


        setIconImage(pics[picIndex]);

    }



}
