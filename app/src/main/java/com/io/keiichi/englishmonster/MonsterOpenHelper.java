package com.io.keiichi.englishmonster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MonsterOpenHelper extends SQLiteOpenHelper {

    public MonsterOpenHelper(Context context) {
        super(context,"TangoDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table tango(english text not null,japanese text );");
        db.execSQL("create table tango(english text not null, japanese text,date text,time,text);");
        db.execSQL("CREATE TABLE monster (name text, level int, exp int, nextexp int, zokusei text);");
        db.execSQL("INSERT INTO monster(name,level,exp,nextexp,zokusei) VALUES('Monster',1,0,60,'nothing');");
//        db.execSQL("INSERT INTO tango(english,japanese) VALUES('English','日本語');");
//        db.execSQL("INSERT INTO tango(english,japanese) VALUES('HOW TO','わかりやすい');");


    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
}

