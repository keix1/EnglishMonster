package com.io.keiichi.englishmonster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExOpenHelper extends SQLiteOpenHelper {

    public ExOpenHelper(Context context) {
        super(context,"ExDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table tango(english text not null,japanese text );");
        db.execSQL("create table experience(" + " when text not null," + "ex integer"
                    + ");");

        db.execSQL("insert into experience(when, ex) values(today,0);");
        db.execSQL("insert into experience(when, ex) values(now,0);");


    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
}

