package com.czh.yujiwidget.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.czh.yujiwidget.App;
import com.czh.yujiwidget.util.GsonUtils;
import com.czh.yujiwidget.util.PrefsUtils;

import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class AutoUpdateService extends Service {

    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;
    private Intent mIntent;
    private Long mTriggerAtTime;

    private String[] mArrayUpdateFrequency = {"HalfAnHour", "AnHour", "TwoHours", "FourHours", "EightHours"};
    private String mUpdateFrequency;
    private int mFrequency = 0;
    private int mUpdateHalfHour = 0;
    private String mWidgetCity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mIntent = new Intent(this, AutoUpdateService.class);
        mPendingIntent = PendingIntent.getService(this, 0, mIntent, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUpdateFrequency = PrefsUtils.getString(getApplicationContext(), "service_frequency_values");
        if (mUpdateFrequency != null) {
            for (int i = 0; i < 5; i++) {
                if (mUpdateFrequency.equals(mArrayUpdateFrequency[i])) {
                    mFrequency = i;
                    break;
                }
            }
        }

        switch (mFrequency) {
            case 0:
                mUpdateHalfHour = 1;
                break;
            case 1:
                mUpdateHalfHour = 2;
                break;
            case 2:
                mUpdateHalfHour = 4;
                break;
            case 3:
                mUpdateHalfHour = 8;
                break;
            case 4:
                mUpdateHalfHour = 16;
                break;
            default:
                break;
        }

        mWidgetCity = PrefsUtils.getString(App.getInstance(), "widgetCity");
        String[] cityInfo = mWidgetCity.split("#"); //"湘桥#潮州--广东--中国#116.63365,23.664675"
        getWeather(cityInfo[2]);

        /*
         * 定时任务
         * */
        mTriggerAtTime = SystemClock.elapsedRealtime() + 30 * 60 * 1000 * mUpdateHalfHour;
        mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, mTriggerAtTime, mPendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mPendingIntent);
        }
    }

    private void getWeather(String cityLocation) {
        HeWeather.getWeather(this, cityLocation, new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(Weather weather) {
                PrefsUtils.putString(AutoUpdateService.this, cityLocation, GsonUtils.toJson(weather));
            }
        });
    }
}
