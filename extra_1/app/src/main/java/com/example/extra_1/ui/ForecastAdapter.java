package com.example.extra_1.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extra_1.R;
import com.example.extra_1.model.WeatherModels;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.VH> {
    private final List<WeatherModels.DayForecast> items = new ArrayList<>();

    public void submit(List<WeatherModels.DayForecast> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView day, temp;
        VH(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.dayName);
            temp = itemView.findViewById(R.id.dayTemp);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_forecast, parent, false);
        return new VH(v);
    }

    @Override public int getItemCount() { return items.size(); }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        WeatherModels.DayForecast item = items.get(position);
        holder.day.setText(item.day);
        holder.temp.setText(item.tempText);
    }
}
