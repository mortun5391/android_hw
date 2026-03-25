package com.example.extra1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.extra1.R
import com.example.extra1.model.DayForecast

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.VH>() {
    private val items = mutableListOf<DayForecast>()
    fun submit(data: List<DayForecast>) { items.clear(); items.addAll(data); notifyDataSetChanged() }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_day_forecast, parent, false)
    ) {
        val day: TextView = itemView.findViewById(R.id.dayName)
        val temp: TextView = itemView.findViewById(R.id.dayTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent)
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.day.text = item.day
        holder.temp.text = item.tempText
    }
}
