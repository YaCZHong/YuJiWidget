package com.czh.yujiwidget.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.czh.yujiwidget.App;
import com.czh.yujiwidget.util.PrefsUtils;
import com.google.gson.Gson;

import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class AutoUpdateJobService extends JobService {
    private String mWidgetCity;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mWidgetCity = PrefsUtils.getString(App.getInstance(), "widgetCity");
        String[] cityInfo = mWidgetCity.split("#"); //"湘桥#潮州--广东--中国#116.63365,23.664675"
        getWeather(jobParameters, cityInfo[2]);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void getWeather(final JobParameters jobParameters, String cityLocation) {
        HeWeather.getWeather(this, cityLocation, new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                jobFinished(jobParameters, false);
            }

            @Override
            public void onSuccess(Weather weather) {
                jobFinished(jobParameters, false);// 如果onStartJob返回true的话需要调用此方法表示任务执行完毕
                PrefsUtils.putString(AutoUpdateJobService.this, cityLocation, new Gson().toJson(weather));
            }
        });
    }
}
