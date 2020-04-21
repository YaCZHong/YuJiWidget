package com.czh.yujiwidget.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.czh.yujiwidget.R;
import com.czh.yujiwidget.adapter.CitiesAdapter;
import com.czh.yujiwidget.bean.City;
import com.czh.yujiwidget.db.CityDatabaseManager;
import com.czh.yujiwidget.util.PrefsUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.appcompat.widget.ListPopupWindow.MATCH_PARENT;

public class CitiesActivity extends BaseActivity {

    private static final String TAG = "CitiesActivity";
    private static final int REQUEST_SEARCH_CITY = 100;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.rv_cities)
    SwipeRecyclerView rvCities;

    private CitiesAdapter mCitiesAdapter;
    private int CHANGE_TYPE_ADD = 1;
    private int CHANGE_TYPE_DELETE = 2;
    private int CHANGE_TYPE_CLICK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CitiesActivity.this, SearchCityActivity.class);
                startActivityForResult(intent, REQUEST_SEARCH_CITY);
            }
        });

        //创建菜单
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(CitiesActivity.this);
                deleteItem.setBackgroundColor(Color.parseColor("#ef5350"));
                deleteItem.setText("删除城市");
                deleteItem.setTextColor(Color.WHITE);
                deleteItem.setWidth((int) dp2px(96));
                deleteItem.setHeight(MATCH_PARENT);
                leftMenu.addMenuItem(deleteItem);

                SwipeMenuItem setWidgetItem = new SwipeMenuItem(CitiesActivity.this);
                setWidgetItem.setBackgroundColor(Color.parseColor("#828282"));
                setWidgetItem.setText("设为小部\n件城市");
                setWidgetItem.setTextColor(Color.WHITE);
                setWidgetItem.setWidth((int) dp2px(96));
                setWidgetItem.setHeight(MATCH_PARENT);
                leftMenu.addMenuItem(setWidgetItem);
            }
        };

        //创建菜单监听器
        OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                // 菜单在Item中的Position，回调方法中的position是recyclerView中的Position;
                int menuPosition = menuBridge.getPosition();

                City city = mCitiesAdapter.getCities().get(position);

                if (menuPosition == 1) {
                    PrefsUtils.putString(CitiesActivity.this, "widgetCity", city.getCityName() + "#" + city.getCityParent() + "#" + city.getCityLocation());
                    Toast.makeText(CitiesActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (mCitiesAdapter.getCities().size() == 1) {
                        showHintDialog("城市列表至少需要存在一个城市，不能为空");
                    } else {
                        deleteHintDialog(position, mCitiesAdapter.getCities().get(position).getCityName());
                    }
                }
            }
        };

        // 设置菜单
        rvCities.setSwipeMenuCreator(mSwipeMenuCreator);
        // 设置监听器
        rvCities.setOnItemMenuClickListener(mItemMenuClickListener);

        rvCities.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition) {
                City city = mCitiesAdapter.getCities().get(adapterPosition);
                StringBuilder sb = new StringBuilder();
                sb.append(city.getCityName())
                        .append("#")
                        .append(city.getCityParent())
                        .append("#")
                        .append(city.getCityLocation());

                PrefsUtils.putString(CitiesActivity.this, "showCity", sb.toString());

                Intent intent = new Intent();
                intent.putExtra("showCityChange", true);
                intent.putExtra("type", CHANGE_TYPE_CLICK);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        rvCities.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#F4F4F4"), MATCH_PARENT, (int) dp2px(0.5f)));
        mCitiesAdapter = new CitiesAdapter(CityDatabaseManager.getInstance().findCities());
        rvCities.setAdapter(mCitiesAdapter);
        rvCities.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult run");
        if (requestCode == REQUEST_SEARCH_CITY && resultCode == RESULT_OK) {
            mCitiesAdapter.setCities(CityDatabaseManager.getInstance().findCities());

            Log.d(TAG,PrefsUtils.getString(CitiesActivity.this, "showCity"));
            Intent intent = new Intent();
            intent.putExtra("showCityChange", true);
            intent.putExtra("type", CHANGE_TYPE_ADD);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void deleteHintDialog(int position, String cityName) {

        City delCity = mCitiesAdapter.getCities().get(position);
        String sbDeleteCity = new StringBuilder()
                .append(delCity.getCityName())
                .append("#")
                .append(delCity.getCityParent())
                .append("#")
                .append(delCity.getCityLocation())
                .toString();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Tip")
                .setMessage("是否确定移除该城市--" + cityName)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CityDatabaseManager.getInstance().deleteCity(delCity);
                        mCitiesAdapter.removeItem(position);

                        PrefsUtils.remove(CitiesActivity.this, sbDeleteCity + "#" + "updateTime");
                        PrefsUtils.remove(CitiesActivity.this, sbDeleteCity + "#" + "updateWeather");
                        PrefsUtils.remove(CitiesActivity.this, sbDeleteCity + "#" + "updateAirNow");

                        String showCity = PrefsUtils.getString(CitiesActivity.this, "showCity");
                        if (showCity != null) {
                            if (sbDeleteCity.equals(showCity)) {
                                ArrayList<City> cities = CityDatabaseManager.getInstance().findCities();
                                City lastCity = cities.get(cities.size() - 1);
                                StringBuilder sb = new StringBuilder();
                                sb.append(lastCity.getCityName())
                                        .append("#")
                                        .append(lastCity.getCityParent())
                                        .append("#")
                                        .append(lastCity.getCityLocation());
                                PrefsUtils.putString(CitiesActivity.this, "showCity", sb.toString());
                                Intent intent = new Intent();
                                intent.putExtra("showCityChange", true);
                                intent.putExtra("type", CHANGE_TYPE_DELETE);
                                setResult(RESULT_OK, intent);
                            }
                        }
                    }
                }).create();
        dialog.show();
    }
}
