package com.online.languages.study.studymaster.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.online.languages.study.studymaster.DBHelper;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.data.NoteData;


public class InfoNotesManager {

    Context context;


    public InfoNotesManager(Context context) {
        this.context= context;
    }


    public void postStartNotes(DBHelper dbHelper, SQLiteDatabase db) {

        postNote(dbHelper, db,  // first note
                context.getString(R.string.first_note_title),
                context.getString(R.string.first_note_text),
                "info.png");

    }


    public void postUpdateNotes(DBHelper dbHelper, SQLiteDatabase db, int version) {

       if (version == 63) {

           postNote(dbHelper, db,  // first note
                   context.getString(R.string.first_note_title),
                   context.getString(R.string.first_note_text),
                   "info.png");

       }


    }


    private void postNote(DBHelper dbHelper, SQLiteDatabase db, String title, String content, String image) {

        NoteData note = new NoteData();
        note.title = title;
        note.content = content;
        note.image = image;
        dbHelper.createNote(db, note);

    }



}