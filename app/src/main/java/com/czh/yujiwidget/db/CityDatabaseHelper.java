package com.czh.yujiwidget.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CityDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city text, "
            + "cityId text)";

    public CityDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
