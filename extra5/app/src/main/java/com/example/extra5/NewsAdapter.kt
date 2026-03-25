package com.example.extra5

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.extra5.db.NewsEntity

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.VH>() {
    private val items = mutableListOf<NewsEntity>()
    fun submit(list: List<NewsEntity>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    class VH(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)) {
        val title: TextView = itemView.findViewById(R.id.newsTitle)
        val desc: TextView = itemView.findViewById(R.id.newsDesc)
        val image: ImageView = itemView.findViewById(R.id.newsImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent)
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.desc.text = item.description
        Glide.with(holder.itemView).load(item.imageUrl).centerCrop().into(holder.image)
    }
}
