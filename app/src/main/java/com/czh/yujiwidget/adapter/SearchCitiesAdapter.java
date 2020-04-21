package com.czh.yujiwidget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czh.yujiwidget.R;
import com.czh.yujiwidget.bean.City;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCitiesAdapter extends RecyclerView.Adapter<SearchCitiesAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private ArrayList<City> cities = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public SearchCitiesAdapter(ArrayList<City> cities) {
        if (cities != null && cities.size() > 0) {
            this.cities.addAll(cities);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_city, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    int position = viewHolder.getAdapterPosition();
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = cities.get(position);
        holder.tvCityName.setText(city.getCityName());
        holder.tvCityParent.setText(city.getCityParent());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_city_name)
        TextView tvCityName;
        @BindView(R.id.tv_city_parent)
        TextView tvCityParent;
        @BindView(R.id.ll_search_city)
        LinearLayout llSearchCity;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setCities(ArrayList<City> cities) {
        if (cities != null && cities.size() > 0) {
            this.cities.clear();
            this.cities.addAll(cities);
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
