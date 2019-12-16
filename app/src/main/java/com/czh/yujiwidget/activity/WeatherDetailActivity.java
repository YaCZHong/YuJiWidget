package com.czh.yujiwidget.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.czh.yujiwidget.R;
import com.czh.yujiwidget.view.Line;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class WeatherDetailActivity extends AppCompatActivity {
    private Line line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        ImmersionBar.with(this)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
        line = findViewById(R.id.line);
        ArrayList<String> arrayListx = new ArrayList<String>();
        arrayListx.add("02时");
        arrayListx.add("05时");
        arrayListx.add("08时");
        arrayListx.add("11时");
        arrayListx.add("14时");
        arrayListx.add("17时");
        arrayListx.add("20时");
        arrayListx.add("23时");
        line.setxValues(arrayListx);

        ArrayList<Float> arrayList = new ArrayList<Float>();
        arrayList.add(22f);
        arrayList.add(23f);
        arrayList.add(23f);
        arrayList.add(24f);
        arrayList.add(22f);
        arrayList.add(21f);
        arrayList.add(21f);
        arrayList.add(19f);

        line.setValues(arrayList);
        line.setChange();

    }
}
