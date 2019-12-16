package com.czh.yujiwidget.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czh.yujiwidget.R;
import com.czh.yujiwidget.activity.WeatherDetailActivity;

import java.util.ArrayList;

import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;

public class CityCardAdapter extends RecyclerView.Adapter<CityCardAdapter.CityCardViewHolder> {

    private ArrayList<Now> cityWeatherArrayList = new ArrayList<>();
    private Context context;

    public CityCardAdapter(Context context, ArrayList<Now> cityWeatherArrayList) {
        this.context = context;
        if (cityWeatherArrayList != null) {
            this.cityWeatherArrayList.addAll(cityWeatherArrayList);
        }
    }

    @NonNull
    @Override
    public CityCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_card, parent, false);
        CityCardViewHolder cityCardViewHolder = new CityCardViewHolder(view);
        cityCardViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, WeatherDetailActivity.class));
            }
        });
        return cityCardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityCardViewHolder holder, int position) {

        Now now = cityWeatherArrayList.get(position);

        if (now != null) {
            holder.tv_cityName.setText(now.getBasic().getLocation());
            holder.iv_weatherIcon.setImageResource(R.mipmap.ic_samsung_sun);
            holder.tv_weather_now.setText(now.getNow().getCond_txt() + "，" + "气温" + now.getNow().getTmp() + "°");
            holder.tv_wind_title.setText(now.getNow().getWind_dir());
            holder.tv_wind_content.setText(now.getNow().getWind_sc() + "级");
            holder.tv_hum_content.setText(now.getNow().getHum() + "%");
            holder.tv_fl_content.setText(now.getNow().getFl() + "°");
        }
    }

    @Override
    public int getItemCount() {
        return cityWeatherArrayList.size();
    }

    static class CityCardViewHolder extends RecyclerView.ViewHolder {

        TextView tv_cityName;
        //        TextView tv_update_time;
        ImageView iv_weatherIcon;
        TextView tv_weather_now;
        TextView tv_wind_title;
        TextView tv_wind_content;
        //        TextView tv_hum_title;
        TextView tv_hum_content;
        //        TextView tv_fl_title;
        TextView tv_fl_content;

        CityCardViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cityName = itemView.findViewById(R.id.tv_city_name);
//            tv_update_time = itemView.findViewById(R.id.tv_update_time);
            iv_weatherIcon = itemView.findViewById(R.id.iv_weather_icon);
            tv_weather_now = itemView.findViewById(R.id.tv_weather_now);
            tv_wind_title = itemView.findViewById(R.id.tv_wind_title);
            tv_wind_content = itemView.findViewById(R.id.tv_wind_content);
//            tv_hum_title = itemView.findViewById(R.id.tv_hum_title);
            tv_hum_content = itemView.findViewById(R.id.tv_hum_content);
//            tv_fl_title = itemView.findViewById(R.id.tv_fl_title);
            tv_fl_content = itemView.findViewById(R.id.tv_fl_content);
        }
    }

    public void setCityWeatherData(ArrayList<Now> cityWeatherArrayList) {
        this.cityWeatherArrayList.clear();
        this.cityWeatherArrayList.addAll(cityWeatherArrayList);
        this.notifyDataSetChanged();
    }
}
