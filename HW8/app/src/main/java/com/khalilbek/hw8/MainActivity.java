package com.khalilbek.hw8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Check if user is already logged in
        SharedPreferences prefs = getSharedPreferences("hw8_prefs", MODE_PRIVATE);
        int currentUserId = prefs.getInt("current_user_id", -1);
        
        if (currentUserId != -1) {
            // User is logged in, go to chat
            startActivity(new Intent(this, ChatActivity.class));
        } else {
            // User is not logged in, go to auth
            startActivity(new Intent(this, AuthActivity.class));
        }
        finish();
    }
}