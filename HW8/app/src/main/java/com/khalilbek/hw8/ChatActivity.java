package com.khalilbek.hw8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.Manifest;
import android.content.pm.PackageManager;
import com.khalilbek.hw8.databinding.ActivityChatBinding;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private AppDatabase database;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private MapView mapView;
    private int currentUserId;
    private String currentUsername;
    
    private static final String USER_AGENT = "HW8_Chat_App";
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configure Osmdroid
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(USER_AGENT);
        
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        database = AppDatabase.getInstance(this);
        
        // Get current user
        SharedPreferences prefs = getSharedPreferences("hw8_prefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("current_user_id", -1);
        currentUsername = prefs.getString("current_username", "Unknown");
        
        if (currentUserId == -1) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }
        
        setupMap();
        setupChat();
        loadMessages();
        
        binding.logoutButton.setOnClickListener(v -> handleLogout());
        binding.sendButton.setOnClickListener(v -> handleSendMessage());
        
        // Request runtime permissions
        requestLocationPermissions();
    }
    
    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            }
        }
    }
    
    private void setupMap() {
        try {
            mapView = binding.mapView;
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setMultiTouchControls(true);
            
            // Set default location (Moscow)
            GeoPoint startPoint = new GeoPoint(55.7558, 37.6173);
            mapView.getController().setZoom(15);
            mapView.getController().setCenter(startPoint);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing map: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupChat() {
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.messagesRecyclerView.setAdapter(messageAdapter);
    }
    
    private void loadMessages() {
        new Thread(() -> {
            try {
                List<Message> loadedMessages = database.messageDao().getAllMessages();
                runOnUiThread(() -> {
                    messages.clear();
                    messages.addAll(loadedMessages);
                    messageAdapter.updateMessages(messages);
                    if (messages.size() > 0) {
                        binding.messagesRecyclerView.scrollToPosition(messages.size() - 1);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> 
                    Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    
    private void handleSendMessage() {
        String messageText = binding.messageInput.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_SHORT).show();
            return;
        }
        
        Message message = new Message(currentUserId, currentUsername, messageText, System.currentTimeMillis());
        
        new Thread(() -> {
            try {
                database.messageDao().insertMessage(message);
                runOnUiThread(() -> {
                    messages.add(message);
                    messageAdapter.updateMessages(messages);
                    binding.messagesRecyclerView.scrollToPosition(messages.size() - 1);
                    binding.messageInput.setText("");
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                    Toast.makeText(ChatActivity.this, "Error sending message", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    
    private void handleLogout() {
        SharedPreferences prefs = getSharedPreferences("hw8_prefs", MODE_PRIVATE);
        prefs.edit().remove("current_user_id").apply();
        prefs.edit().remove("current_username").apply();
        
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Recreate the view hierarchy to apply the new layout
        recreate();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
}
