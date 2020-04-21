package com.czh.yujiwidget.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.czh.yujiwidget.App;
import com.czh.yujiwidget.bean.City;

import java.util.ArrayList;

public class CityDatabaseManager {
    private volatile static CityDatabaseManager cityDBMgr;
    private CityDatabaseHelper dbHelper;

    private CityDatabaseManager() {
        dbHelper = new CityDatabaseHelper(App.getInstance(), "city.db", null, 1);
    }

    public static CityDatabaseManager getInstance() {
        if (cityDBMgr == null) {
            synchronized (CityDatabaseManager.class) {
                if (cityDBMgr == null) {
                    cityDBMgr = new CityDatabaseManager();
                }
            }

        }
        return cityDBMgr;
    }


    //添加城市
    public synchronized void saveCity(City city) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("City", null, "cityName = ? and cityParent = ?", new String[]{city.getCityName(), city.getCityParent()}, null, null, null);
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put("cityLocation", city.getCityLocation());
                db.update("City", values, "cityName = ? and cityParent = ?", new String[]{city.getCityName(), city.getCityParent()});
                Toast.makeText(App.getInstance(), "城市已存在，仅更新经纬度信息", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues values = new ContentValues();
                values.put("cityName", city.getCityName());
                values.put("cityParent", city.getCityParent());
                values.put("cityLocation", city.getCityLocation());
                db.insert("City", null, values);
                Toast.makeText(App.getInstance(), "城市添加成功", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }

    //删除城市
    public synchronized void deleteCity(City city) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("City", "cityName = ? and cityParent = ? and cityLocation = ?", new String[]{city.getCityName(), city.getCityParent(), city.getCityLocation()});
            Toast.makeText(App.getInstance(), "城市删除成功", Toast.LENGTH_SHORT).show();
        }
    }

    //查询所有城市
    public synchronized ArrayList<City> findCities() {

        ArrayList<City> cities = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query("City", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
                    String cityParent = cursor.getString(cursor.getColumnIndex("cityParent"));
                    String cityLocation = cursor.getString(cursor.getColumnIndex("cityLocation"));

                    City city = new City();
                    city.setCityName(cityName);
                    city.setCityParent(cityParent);
                    city.setCityLocation(cityLocation);
                    cities.add(city);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return cities;
    }
}
