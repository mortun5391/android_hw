package com.example.extra5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.extra5.db.NewsEntity;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {
    private final List<NewsEntity> items = new ArrayList<>();

    public void submit(List<NewsEntity> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, desc;
        ImageView image;
        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            desc = itemView.findViewById(R.id.newsDesc);
            image = itemView.findViewById(R.id.newsImage);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
    }
    @Override public int getItemCount() { return items.size(); }

    @Override public void onBindViewHolder(@NonNull VH holder, int position) {
        NewsEntity item = items.get(position);
        holder.title.setText(item.title);
        holder.desc.setText(item.description);
        Glide.with(holder.itemView).load(item.imageUrl).centerCrop().into(holder.image);
    }
}
