package com.czh.yujiwidget.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.czh.yujiwidget.R;
import com.czh.yujiwidget.adapter.CityCardAdapter;
import com.czh.yujiwidget.db.CityDatabaseHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CityDatabaseHelper cityDatabaseHelper;
    private SQLiteDatabase db;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private CityCardAdapter cityCardAdapter;
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<Now> cityWeatherList = new ArrayList<>();

    private int times = 0;

    // 该方法用于检测所有城市天气是否获取完成
    private void check() {
        times++;
        if (times == cityList.size()) {
            times = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    smartRefreshLayout.finishRefresh();
                    cityCardAdapter.setCityWeatherData(cityWeatherList);
                    cityWeatherList.clear();
                }
            });
        } else
            getCityWeatherNow(times);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smartRefreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.rv);

        HeConfig.init("HE1912141000071985", "0d3c2b99d10142a2b3cda304a1ae02fc");
        HeConfig.switchToFreeServerNode();

        cityDatabaseHelper = new CityDatabaseHelper(this, "city.db", null, 1);
        db = cityDatabaseHelper.getWritableDatabase();
//        cityList = queryCitys(db);
        cityList.add("CN101010100");
        cityList.add("CN101020100");
        cityList.add("CN101300501");

        cityCardAdapter = new CityCardAdapter(this, null);
        recyclerView.setAdapter(cityCardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (cityList.size() != 0) {
            smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    getCityWeatherNow(0);
                }
            });
        }
    }

    private ArrayList<String> queryCitys(SQLiteDatabase db) {
        ArrayList<String> cityList = new ArrayList<>();
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String cityId = cursor.getString(cursor.getColumnIndex("cityId"));

                cityList.add(city + "--" + cityId);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cityList;
    }

    private void getCityWeatherNow(int index) {

        HeWeather.getWeatherNow(MainActivity.this, cityList.get(index), new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                check();
            }

            @Override
            public void onSuccess(Now now) {
                cityWeatherList.add(now);
                check();
            }
        });
    }
}
