package com.example.student;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 17822 on 2020/10/25.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "StudentManage.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("abcde","oncreate is calling!");
        db.execSQL("create table student(id integer primary key autoincrement,name varchar(20),sex varchar(10),age integer,phone varchar(20),college varchar(30), banji varchar(30),adress varchar(30))");
        db.execSQL("create table user(_id integer primary key autoincrement,username varchar(20) not null unique,password varchar(20),permission varchar(15))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.e("abcde","onUpgrade is calling!");
    }
}
