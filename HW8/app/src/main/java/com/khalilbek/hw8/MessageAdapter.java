package com.khalilbek.hw8;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khalilbek.hw8.databinding.ItemMessageBinding;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> messages;
    private SimpleDateFormat dateFormat;
    
    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
        this.dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.binding.messageUsername.setText(message.username);
        holder.binding.messageContent.setText(message.content);
        holder.binding.messageTime.setText(dateFormat.format(new Date(message.timestamp)));
    }
    
    @Override
    public int getItemCount() {
        return messages.size();
    }
    
    public void updateMessages(List<Message> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMessageBinding binding;
        
        ViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
