package com.czh.yujiwidget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.czh.yujiwidget.R;
import com.czh.yujiwidget.adapter.SearchCitiesAdapter;
import com.czh.yujiwidget.bean.City;
import com.czh.yujiwidget.db.CityDatabaseManager;
import com.czh.yujiwidget.util.PrefsUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class SearchCityActivity extends BaseActivity {
    private static final String TAG = "SearchCityActivity";

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.rv_search_city)
    RecyclerView rvSearchCity;

    private ArrayList<City> cities = new ArrayList<>();
    private SearchCitiesAdapter searchCitiesAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        ButterKnife.bind(this);
        toolBar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
        toolBar.setTitle("添加城市");
        setSupportActionBar(toolBar);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showProgressDialog("正在搜索中...");
                HeWeather.getSearch(SearchCityActivity.this, query, "cn", 10, Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        closeProgressDialog();
                        showHintDialog("搜索失败");
                    }

                    @Override
                    public void onSuccess(Search search) {
                        if (search.getBasic() != null && search.getBasic().size() != 0) {
                            cities.clear();
                            for (Basic basic : search.getBasic()) {
                                City city = new City();
                                city.setCityName(basic.getLocation());
                                city.setCityParent(basic.getParent_city() + "--" + basic.getAdmin_area() + "--" + basic.getCnty());
                                city.setCityLocation(basic.getLon() + "," + basic.getLat());
                                cities.add(city);
                            }

                            if (searchCitiesAdapter != null) {
                                searchCitiesAdapter.setCities(cities);
                            }
                        } else {
                            showHintDialog("搜索失败，目前只支持到区、县级及以上的城市");
                        }
                        closeProgressDialog();
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchCitiesAdapter = new SearchCitiesAdapter(null);
        searchCitiesAdapter.setOnItemClickListener(new SearchCitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                City city = cities.get(position);
                CityDatabaseManager.getInstance().saveCity(city);
//                if (CityDatabaseManager.getInstance().findCities().size() == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append(city.getCityName())
                        .append("#")
                        .append(city.getCityParent())
                        .append("#")
                        .append(city.getCityLocation());

                PrefsUtils.putString(SearchCityActivity.this, "showCity", sb.toString());
//                }
                setResult(RESULT_OK);
                finish();
            }
        });
        rvSearchCity.setAdapter(searchCitiesAdapter);
        rvSearchCity.setLayoutManager(new LinearLayoutManager(this));

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressDialog("正在加载中...");
                HeWeather.getSearch(SearchCityActivity.this, "潮州", "cn", 20, Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        closeProgressDialog();
                        showHintDialog("加载失败");
                    }

                    @Override
                    public void onSuccess(Search search) {
                        if (search.getBasic() != null && search.getBasic().size() != 0) {
                            cities.clear();
                            for (Basic basic : search.getBasic()) {
                                City city = new City();
                                city.setCityName(basic.getLocation());
                                city.setCityParent(basic.getParent_city() + "--" + basic.getAdmin_area() + "--" + basic.getCnty());
                                city.setCityLocation(basic.getLon() + "," + basic.getLat());
                                cities.add(city);
                            }

                            if (searchCitiesAdapter != null) {
                                searchCitiesAdapter.setCities(cities);
                            }
                        } else {
                            showHintDialog("加载失败");
                        }
                        closeProgressDialog();
                    }
                });
            }
        }, 400);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_city, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    /**
     * 点击返回键的时候会调用
     */
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
