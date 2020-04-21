package com.czh.yujiwidget.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.czh.yujiwidget.R;
import com.czh.yujiwidget.bean.City;
import com.czh.yujiwidget.db.CityDatabaseManager;
import com.czh.yujiwidget.util.GetTimeUtils;
import com.czh.yujiwidget.util.GsonUtils;
import com.czh.yujiwidget.util.IconUtils;
import com.czh.yujiwidget.util.PrefsUtils;
import com.czh.yujiwidget.util.StatusBarUtils;
import com.czh.yujiwidget.view.Line;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_weather_tmp)
    TextView tvWeatherTmp;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.line)
    Line line;
    @BindView(R.id.cv_hourly)
    CardView cvHourly;
    @BindView(R.id.cv_daily)
    CardView cvDaily;
    @BindView(R.id.iv_comf)
    ImageView ivComf;
    @BindView(R.id.tv_comf_title)
    TextView tvComfTitle;
    @BindView(R.id.tv_comf_content)
    TextView tvComfContent;
    @BindView(R.id.iv_drsg)
    ImageView ivDrsg;
    @BindView(R.id.tv_drsg_title)
    TextView tvDrsgTitle;
    @BindView(R.id.tv_drsg_content)
    TextView tvDrsgContent;
    @BindView(R.id.iv_flu)
    ImageView ivFlu;
    @BindView(R.id.tv_flu_title)
    TextView tvFluTitle;
    @BindView(R.id.tv_flu_content)
    TextView tvFluContent;
    @BindView(R.id.iv_sport)
    ImageView ivSport;
    @BindView(R.id.tv_sport_title)
    TextView tvSportTitle;
    @BindView(R.id.tv_sport_content)
    TextView tvSportContent;
    @BindView(R.id.iv_trav)
    ImageView ivTrav;
    @BindView(R.id.tv_trav_title)
    TextView tvTravTitle;
    @BindView(R.id.tv_trav_content)
    TextView tvTravContent;
    @BindView(R.id.iv_uv)
    ImageView ivUv;
    @BindView(R.id.tv_uv_title)
    TextView tvUvTitle;
    @BindView(R.id.tv_uv_content)
    TextView tvUvContent;
    @BindView(R.id.iv_cw)
    ImageView ivCw;
    @BindView(R.id.tv_cw_title)
    TextView tvCwTitle;
    @BindView(R.id.tv_cw_content)
    TextView tvCwContent;
    @BindView(R.id.iv_air)
    ImageView ivAir;
    @BindView(R.id.tv_air_title)
    TextView tvAirTitle;
    @BindView(R.id.tv_air_content)
    TextView tvAirContent;
    @BindView(R.id.cv_other)
    CardView cvOther;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rl_hourly_more)
    RelativeLayout rlHourlyMore;
    @BindView(R.id.rl_daily_more)
    RelativeLayout rlDailyMore;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_main_bg)
    ImageView ivMainBg;
    @BindView(R.id.tv_day1)
    TextView tvDay1;
    @BindView(R.id.tv_day1_tmp_max)
    TextView tvDay1TmpMax;
    @BindView(R.id.tv_day1_tmp_min)
    TextView tvDay1TmpMin;
    @BindView(R.id.ll_day_1)
    LinearLayout llDay1;
    @BindView(R.id.tv_day2)
    TextView tvDay2;
    @BindView(R.id.tv_day2_tmp_max)
    TextView tvDay2TmpMax;
    @BindView(R.id.tv_day2_tmp_min)
    TextView tvDay2TmpMin;
    @BindView(R.id.ll_day_2)
    LinearLayout llDay2;
    @BindView(R.id.tv_day3)
    TextView tvDay3;
    @BindView(R.id.tv_day3_tmp_max)
    TextView tvDay3TmpMax;
    @BindView(R.id.tv_day3_tmp_min)
    TextView tvDay3TmpMin;
    @BindView(R.id.ll_day_3)
    LinearLayout llDay3;
    @BindView(R.id.tv_day4)
    TextView tvDay4;
    @BindView(R.id.tv_day4_tmp_max)
    TextView tvDay4TmpMax;
    @BindView(R.id.tv_day4_tmp_min)
    TextView tvDay4TmpMin;
    @BindView(R.id.ll_day_4)
    LinearLayout llDay4;
    @BindView(R.id.tv_day5)
    TextView tvDay5;
    @BindView(R.id.tv_day5_tmp_max)
    TextView tvDay5TmpMax;
    @BindView(R.id.tv_day5_tmp_min)
    TextView tvDay5TmpMin;
    @BindView(R.id.ll_day_5)
    LinearLayout llDay5;
    @BindView(R.id.tv_hour1_state)
    TextView tvHour1State;
    @BindView(R.id.tv_hour2_state)
    TextView tvHour2State;
    @BindView(R.id.tv_hour3_state)
    TextView tvHour3State;
    @BindView(R.id.tv_hour4_state)
    TextView tvHour4State;
    @BindView(R.id.tv_hour5_state)
    TextView tvHour5State;
    @BindView(R.id.tv_hour6_state)
    TextView tvHour6State;
    @BindView(R.id.tv_hour7_state)
    TextView tvHour7State;
    @BindView(R.id.tv_hour8_state)
    TextView tvHour8State;
    @BindView(R.id.iv_hour1_state)
    ImageView ivHour1State;
    @BindView(R.id.iv_hour2_state)
    ImageView ivHour2State;
    @BindView(R.id.iv_hour3_state)
    ImageView ivHour3State;
    @BindView(R.id.iv_hour4_state)
    ImageView ivHour4State;
    @BindView(R.id.iv_hour5_state)
    ImageView ivHour5State;
    @BindView(R.id.iv_hour6_state)
    ImageView ivHour6State;
    @BindView(R.id.iv_hour7_state)
    ImageView ivHour7State;
    @BindView(R.id.iv_hour8_state)
    ImageView ivHour8State;
    @BindView(R.id.tv_hourly_more)
    TextView tvHourlyMore;
    @BindView(R.id.tv_daily_more)
    TextView tvDailyMore;
    @BindView(R.id.tv_comf_brf)
    TextView tvComfBrf;
    @BindView(R.id.rl_comf)
    RelativeLayout rlComf;
    @BindView(R.id.tv_drsg_brf)
    TextView tvDrsgBrf;
    @BindView(R.id.rl_drsg)
    RelativeLayout rlDrsg;
    @BindView(R.id.tv_flu_brf)
    TextView tvFluBrf;
    @BindView(R.id.rl_flu)
    RelativeLayout rlFlu;
    @BindView(R.id.tv_sport_brf)
    TextView tvSportBrf;
    @BindView(R.id.rl_sport)
    RelativeLayout rlSport;
    @BindView(R.id.tv_trav_brf)
    TextView tvTravBrf;
    @BindView(R.id.rl_trav)
    RelativeLayout rlTrav;
    @BindView(R.id.tv_uv_brf)
    TextView tvUvBrf;
    @BindView(R.id.rl_uv)
    RelativeLayout rlUv;
    @BindView(R.id.tv_cw_brf)
    TextView tvCwBrf;
    @BindView(R.id.rl_cw)
    RelativeLayout rlCw;
    @BindView(R.id.tv_air_brf)
    TextView tvAirBrf;
    @BindView(R.id.rl_air)
    RelativeLayout rlAir;
    @BindView(R.id.tv_aqi)
    TextView tvAqi;
    @BindView(R.id.tv_aqi_qlty)
    TextView tvAqiQlty;
    @BindView(R.id.tv_aqi_pm25)
    TextView tvAqiPm25;
    @BindView(R.id.tv_aqi_pm10)
    TextView tvAqiPm10;
    @BindView(R.id.tv_aqi_no2)
    TextView tvAqiNo2;
    @BindView(R.id.tv_aqi_so2)
    TextView tvAqiSo2;
    @BindView(R.id.cv_aqi)
    CardView cvAqi;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.tv_day1_txt)
    TextView tvDay1Txt;
    @BindView(R.id.tv_day2_txt)
    TextView tvDay2Txt;
    @BindView(R.id.tv_day3_txt)
    TextView tvDay3Txt;
    @BindView(R.id.tv_day4_txt)
    TextView tvDay4Txt;
    @BindView(R.id.tv_day5_txt)
    TextView tvDay5Txt;

    private ArrayList<TextView> mHourlyTVList = new ArrayList<>();
    private ArrayList<ImageView> mHourlyIVList = new ArrayList<>();
    private ArrayList<TextView> mDailyTxtList = new ArrayList<>();
    private ArrayList<TextView> mDailyDateTVList = new ArrayList<>();
    private ArrayList<TextView> mDailyTmpMaxTVList = new ArrayList<>();
    private ArrayList<TextView> mDailyTmpMinTVList = new ArrayList<>();

    private static final String TAG = "MainActivity";
    private static final int REQUEST_AUTHORITY_PERMISSION = 100;
    private static final int REQUEST_CITIES_CODE = 200;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;

    private String mShowCity;
    private int mScrollY = 0;
    private AnimationSet mAnimationSet;
    private Animation mTranslate;
    private Animation mAlpha;

    private String[] permissionList = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private List<String> requestPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //和风
        HeConfig.init("HE1912141000071985", "0d3c2b99d10142a2b3cda304a1ae02fc");
        HeConfig.switchToFreeServerNode();

        //状态栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        StatusBarUtils.immersive(this);
        StatusBarUtils.darkMode(this, true);
        StatusBarUtils.setPaddingSmart(this, toolbar);

        initView();
        initLocation();
        initAnimation();

        requestPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                showProgressDialog("正在定位中...");
                mLocationClient.startLocation();
                break;
            case R.id.city_manage:
                toCitiesActivityForResult();
                break;
            case R.id.settings:
                break;
            case R.id.about:
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    /**
     * 请求权限结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] pemissions, int[] grantResults) {
        if (requestCode == REQUEST_AUTHORITY_PERMISSION) {
            if (grantResults.length > 0) {
                int noAllowPermissionCount = 0;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        noAllowPermissionCount++;
                    }
                }

                if (noAllowPermissionCount != 0) {
                    showToSystemSettingsDialog();
                } else {
                    checkCityAndGetWeather();
                }
            }
        }
    }

    /**
     * 检查权限并请求
     */
    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionList.clear();
            for (String s : permissionList) {
                if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionList.add(s);
                }
            }

            if (!requestPermissionList.isEmpty()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showRequestPermissionDialog();
                } else {
                    String[] permissions = requestPermissionList.toArray(new String[requestPermissionList.size()]);
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_AUTHORITY_PERMISSION);
                }
            } else {
                checkCityAndGetWeather();
            }
        } else {
            checkCityAndGetWeather();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult run");
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CITIES_CODE && data.getBooleanExtra("showCityChange", false)) {
//                int type = data.getIntExtra("type",0);
                mShowCity = PrefsUtils.getString(MainActivity.this, "showCity");
                Log.d(TAG, mShowCity);
                smartGetWeather(mShowCity);
            }
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        toolbar.setBackgroundColor(0);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int h = (int) dp2px(242);
            private int lastScrollY = 0;
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.main_colorPrimary) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > dp2px(292)) {
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }

                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                }
                lastScrollY = scrollY;
            }
        });

        swipeRefreshLayout.setProgressViewEndTarget(false, (int) dp2px(100));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!TextUtils.isEmpty(mShowCity)) {
                    getWeatherFromInternet(mShowCity);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    toCitiesActivityForResult();
                }
            }
        });

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCitiesActivityForResult();
            }
        });
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCitiesActivityForResult();
            }
        });

        mHourlyTVList.add(tvHour1State);
        mHourlyTVList.add(tvHour2State);
        mHourlyTVList.add(tvHour3State);
        mHourlyTVList.add(tvHour4State);
        mHourlyTVList.add(tvHour5State);
        mHourlyTVList.add(tvHour6State);
        mHourlyTVList.add(tvHour7State);
        mHourlyTVList.add(tvHour8State);

        mHourlyIVList.add(ivHour1State);
        mHourlyIVList.add(ivHour2State);
        mHourlyIVList.add(ivHour3State);
        mHourlyIVList.add(ivHour4State);
        mHourlyIVList.add(ivHour5State);
        mHourlyIVList.add(ivHour6State);
        mHourlyIVList.add(ivHour7State);
        mHourlyIVList.add(ivHour8State);

        mDailyDateTVList.add(tvDay1);
        mDailyDateTVList.add(tvDay2);
        mDailyDateTVList.add(tvDay3);
        mDailyDateTVList.add(tvDay4);
        mDailyDateTVList.add(tvDay5);

        mDailyTxtList.add(tvDay1Txt);
        mDailyTxtList.add(tvDay2Txt);
        mDailyTxtList.add(tvDay3Txt);
        mDailyTxtList.add(tvDay4Txt);
        mDailyTxtList.add(tvDay5Txt);

        mDailyTmpMaxTVList.add(tvDay1TmpMax);
        mDailyTmpMaxTVList.add(tvDay2TmpMax);
        mDailyTmpMaxTVList.add(tvDay3TmpMax);
        mDailyTmpMaxTVList.add(tvDay4TmpMax);
        mDailyTmpMaxTVList.add(tvDay5TmpMax);

        mDailyTmpMinTVList.add(tvDay1TmpMin);
        mDailyTmpMinTVList.add(tvDay2TmpMin);
        mDailyTmpMinTVList.add(tvDay3TmpMin);
        mDailyTmpMinTVList.add(tvDay4TmpMin);
        mDailyTmpMinTVList.add(tvDay5TmpMin);

        Glide.with(this).load(R.drawable.weather_detail_bg).into(ivMainBg);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 设置定位监听
        mLocationClient.setLocationListener(locationListener);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                closeProgressDialog();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    mLocationClient.stopLocation();

                    String district = location.getDistrict();
                    String amapCity = location.getCity();
                    String province = location.getProvince();
                    String country = location.getCountry();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    StringBuilder sb = new StringBuilder();
                    sb.append(amapCity.substring(0, amapCity.length() - 1))
                            .append("--")
                            .append(province.substring(0, province.length() - 1))
                            .append("--")
                            .append(country);

                    City city = new City();
                    city.setCityName(district.substring(0, district.length() - 1));
                    city.setCityParent(sb.toString());
                    city.setCityLocation(longitude + "," + latitude);

                    CityDatabaseManager.getInstance().saveCity(city);

                    mShowCity = city.getCityName() + "#" + city.getCityParent() + "#" + city.getCityLocation();
                    PrefsUtils.putString(MainActivity.this, "showCity", mShowCity);
                    smartGetWeather(mShowCity);

                    Log.e("AmapSuccess", "location Success:"
                            + location.getDistrict() + "\n"
                            + location.getCity() + "--" + location.getProvince() + "--" + location.getCountry() + "\n"
                            + location.getLongitude() + "," + location.getLatitude());

                } else {

                    mLocationClient.stopLocation();
                    showHintDialog("定位失败：" + location.getErrorInfo());
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, errCode:"
                            + location.getErrorCode() + ", errInfo:"
                            + location.getErrorInfo());
                }
            }
        }
    };

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    /**
     * 初始化动画参数
     */
    private void initAnimation() {
        // 组合动画
        mAnimationSet = new AnimationSet(false);

        // 平移动画
        mTranslate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0.02f
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        mTranslate.setDuration(600);

        // 透明度动画
        mAlpha = new AlphaAnimation(0, 1);
        mAlpha.setDuration(600);

        mAnimationSet.addAnimation(mTranslate);
        mAnimationSet.addAnimation(mAlpha);
        mAnimationSet.setInterpolator(new DecelerateInterpolator());
    }

    private void checkCityAndGetWeather() {
        Log.d(TAG, "checkCityAndGetWeather");
        mShowCity = PrefsUtils.getString(MainActivity.this, "showCity");

        //有指定显示的城市
        if (mShowCity != null) {
            smartGetWeather(mShowCity);
        } else {
            //没有指定城市则拿最后一个
            ArrayList<City> mCities = CityDatabaseManager.getInstance().findCities();
            if (mCities.size() != 0) {
                City city = mCities.get(mCities.size() - 1);
                StringBuilder sb = new StringBuilder();
                sb.append(city.getCityName())
                        .append("#")
                        .append(city.getCityParent())
                        .append("#")
                        .append(city.getCityLocation());
                mShowCity = sb.toString();
                PrefsUtils.putString(MainActivity.this, "showCity", mShowCity);
                smartGetWeather(mShowCity);
            } else {
                showNoneCityDialog();
            }
        }
    }

    private void toCitiesActivityForResult() {
        Intent intent = new Intent(MainActivity.this, CitiesActivity.class);
        startActivityForResult(intent, REQUEST_CITIES_CODE);
    }

    @Override
    public void showHintDialog(String msg) {
        AlertDialog hintDialog = new AlertDialog.Builder(this)
                .setTitle("Tip")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toCitiesActivityForResult();
                    }
                }).create();
        hintDialog.show();
    }

    private void showRequestPermissionDialog() {
        AlertDialog requestPermissionDialog = new AlertDialog.Builder(this)
                .setTitle("Tip")
                .setMessage("程序的正常运行需要如下的两个权限：\n1、定位权限：用于获取您当前的位置信息以便获取当前位置的天气数据；\n2、存储权限：用于保存相关的城市信息和天气数据。")
                .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] permissions = requestPermissionList.toArray(new String[requestPermissionList.size()]);
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_AUTHORITY_PERMISSION);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        requestPermissionDialog.show();
    }

    private void showToSystemSettingsDialog() {
        AlertDialog toSystemSettingsDialog = new AlertDialog.Builder(this)
                .setTitle("Tip")
                .setMessage("缺少必要的相关权限，可能导致部分功能使用异常，是否前往设置页面开启？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        toSystemSettingsDialog.show();
    }

    private void showNoneCityDialog() {
        AlertDialog noneCityDialog = new AlertDialog.Builder(this)
                .setTitle("Tip")
                .setMessage("城市列表为空，请选择下面任一方式来添加城市")
                .setCancelable(false)
                .setPositiveButton("定位", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog("正在定位中...");
                        mLocationClient.startLocation();
                    }
                }).setNegativeButton("进入城市管理页", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toCitiesActivityForResult();
                    }
                }).create();
        noneCityDialog.show();
    }

    private void smartGetWeather(String cityStr) {

        Weather updateWeather = GsonUtils.fromJson(PrefsUtils.getString(MainActivity.this, cityStr + "#" + "updateWeather"), Weather.class);
        AirNow updateAirNow = GsonUtils.fromJson(PrefsUtils.getString(MainActivity.this, cityStr + "#" + "updateAirNow"), AirNow.class);
        String updateTime = PrefsUtils.getString(MainActivity.this, cityStr + "#" + "updateTime");

        if (updateWeather != null) { //区县城市没有Air数据，所以此处无需判断，在showWeatherAir判断即可

            //这里先展示旧数据可以避免UI无数据的问题
            showWeather(updateWeather, cityStr);
            showWeatherAir(updateAirNow);

            String nowTime = GetTimeUtils.getCurrentTime();
            String nowHour = nowTime.substring(0, 2);
            String updateHour = updateTime.substring(0, 2);

            if ((Math.abs(Integer.parseInt(nowTime.substring(3)) - Integer.parseInt(updateTime.substring(3))) > 9) || !(nowHour.equals(updateHour))) {
                getWeatherFromInternet(cityStr);
            }
        } else {
            getWeatherFromInternet(cityStr);
        }
    }

    private void getWeatherFromInternet(String cityStr) {

        String[] cityInfo = cityStr.split("#");

        swipeRefreshLayout.setRefreshing(true);

        HeWeather.getWeather(this, cityInfo[2], new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Weather weather) {

                PrefsUtils.putString(MainActivity.this, cityStr + "#" + "updateTime", GetTimeUtils.getCurrentTime());
                PrefsUtils.putString(MainActivity.this, cityStr + "#" + "updateWeather", GsonUtils.toJson(weather));
                showWeather(weather, cityStr);

                HeWeather.getAirNow(MainActivity.this, cityInfo[2], new HeWeather.OnResultAirNowBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        showWeatherAir(null);
                    }

                    @Override
                    public void onSuccess(AirNow airNow) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (airNow != null && airNow.getAir_now_city() != null) {
                            PrefsUtils.putString(MainActivity.this, cityStr + "#" + "updateAirNow", GsonUtils.toJson(airNow));
                            showWeatherAir(airNow);
                        } else {
                            showWeatherAir(null);
                        }
                    }
                });
            }
        });
    }

    private void showWeather(Weather weather, String cityStr) {

        String updateTime = PrefsUtils.getString(MainActivity.this, cityStr + "#" + "updateTime");
        if (!TextUtils.isEmpty(updateTime)) {
            tvUpdateTime.setText(updateTime);
        }

        tvTitle.setText(weather.getBasic().getLocation() + "  " + weather.getNow().getCond_txt() + "  " + weather.getNow().getTmp() + "°");
        tvCity.setText(weather.getBasic().getLocation() + "  " + weather.getNow().getCond_txt());
        tvWeatherTmp.setText(weather.getNow().getTmp() + "°");
        tvOther.setText(weather.getNow().getWind_dir() + weather.getNow().getWind_sc() + "级，湿度" + weather.getNow().getHum() + "%，体感" + weather.getNow().getFl() + "°");

        // 逐三小时天气
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Float> values = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            mHourlyTVList.get(i).setText(weather.getHourly().get(i).getCond_txt());

            String time = weather.getHourly().get(i).getTime();
            time = time.substring(time.length() - 5, time.length() - 3);
            int hourNow = Integer.parseInt(time);
            if (hourNow >= 6 && hourNow <= 18) {
                Glide.with(MainActivity.this).load(IconUtils.getDayIconDark(weather.getHourly().get(i).getCond_code())).into(mHourlyIVList.get(i));
            } else {
                Glide.with(MainActivity.this).load(IconUtils.getNightIconDark(weather.getHourly().get(i).getCond_code())).into(mHourlyIVList.get(i));
            }

            xValues.add(weather.getHourly().get(i).getTime().substring(10));
            values.add(Float.parseFloat(weather.getHourly().get(i).getTmp()));
        }
        line.setxValues(xValues);
        line.setValues(values);
        line.setChange();

        // 逐日天气
        for (int i = 0; i < 5; i++) {
            mDailyDateTVList.get(i).setText(weather.getDaily_forecast().get(i).getDate().substring(5));
            if(weather.getDaily_forecast().get(i).getCond_txt_d().equals(weather.getDaily_forecast().get(i).getCond_txt_n())){
                mDailyTxtList.get(i).setText(weather.getDaily_forecast().get(i).getCond_txt_d());
            }else {
                mDailyTxtList.get(i).setText(weather.getDaily_forecast().get(i).getCond_txt_d()+"转"+weather.getDaily_forecast().get(i).getCond_txt_n());
            }
            mDailyTmpMaxTVList.get(i).setText(weather.getDaily_forecast().get(i).getTmp_max() + "°/");
            mDailyTmpMinTVList.get(i).setText(weather.getDaily_forecast().get(i).getTmp_min() + "°");
        }

        // 生活指数
        tvComfBrf.setText(weather.getLifestyle().get(0).getBrf());
        tvComfContent.setText(weather.getLifestyle().get(0).getTxt());
        tvDrsgBrf.setText(weather.getLifestyle().get(1).getBrf());
        tvDrsgContent.setText(weather.getLifestyle().get(1).getTxt());
        tvFluBrf.setText(weather.getLifestyle().get(2).getBrf());
        tvFluContent.setText(weather.getLifestyle().get(2).getTxt());
        tvSportBrf.setText(weather.getLifestyle().get(3).getBrf());
        tvSportContent.setText(weather.getLifestyle().get(3).getTxt());
        tvTravBrf.setText(weather.getLifestyle().get(4).getBrf());
        tvTravContent.setText(weather.getLifestyle().get(4).getTxt());
        tvUvBrf.setText(weather.getLifestyle().get(5).getBrf());
        tvUvContent.setText(weather.getLifestyle().get(5).getTxt());
        tvCwBrf.setText(weather.getLifestyle().get(6).getBrf());
        tvCwContent.setText(weather.getLifestyle().get(6).getTxt());
        tvAirBrf.setText(weather.getLifestyle().get(7).getBrf());
        tvAirContent.setText(weather.getLifestyle().get(7).getTxt());

        //滑动到顶部、动画
        scrollView.smoothScrollTo(0, 0);
        scrollView.startAnimation(mAnimationSet);
    }

    private void showWeatherAir(AirNow airNow) {
        if (airNow != null) {
            cvAqi.setVisibility(View.VISIBLE);
            tvAqi.setText(airNow.getAir_now_city().getAqi());
            tvAqiQlty.setText(airNow.getAir_now_city().getQlty());
            tvAqiPm25.setText(airNow.getAir_now_city().getPm25());
            tvAqiPm10.setText(airNow.getAir_now_city().getPm10());
            tvAqiNo2.setText(airNow.getAir_now_city().getNo2());
            tvAqiSo2.setText(airNow.getAir_now_city().getSo2());
        } else {
            cvAqi.setVisibility(View.GONE);
            tvAqi.setText("✘");
            tvAqiQlty.setText("暂无数据");
            tvAqiPm25.setText("--/--");
            tvAqiPm10.setText("--/--");
            tvAqiNo2.setText("--/--");
            tvAqiSo2.setText("--/--");
        }
    }
}
