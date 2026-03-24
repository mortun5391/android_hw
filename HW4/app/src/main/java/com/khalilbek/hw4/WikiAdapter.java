package com.khalilbek.hw4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WikiAdapter extends RecyclerView.Adapter<WikiAdapter.WikiViewHolder> {

    private final List<WikiItem> data = new ArrayList<>();

    public void setItems(List<WikiItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WikiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new WikiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WikiViewHolder holder, int position) {
        WikiItem item = data.get(position);
        holder.titleText.setText(item.getHeading());
        holder.bodyText.setText(android.text.Html.fromHtml(item.getBody(), android.text.Html.FROM_HTML_MODE_LEGACY));
        holder.bodyText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class WikiViewHolder extends RecyclerView.ViewHolder {
        final TextView titleText;
        final TextView bodyText;

        WikiViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            bodyText = itemView.findViewById(R.id.bodyText);
        }
    }
}
