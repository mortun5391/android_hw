package com.khalilbek.hw8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.khalilbek.hw8.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private AppDatabase database;
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        database = AppDatabase.getInstance(this);
        
        binding.toggleMode.setOnClickListener(v -> toggleMode());
        binding.submitButton.setOnClickListener(v -> handleSubmit());
    }
    
    private void toggleMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            binding.authTitle.setText(R.string.login_title);
            binding.emailContainer.setVisibility(android.view.View.GONE);
            binding.submitButton.setText(R.string.login_button);
            binding.toggleMode.setText(R.string.toggle_to_register);
        } else {
            binding.authTitle.setText(R.string.register_title);
            binding.emailContainer.setVisibility(android.view.View.VISIBLE);
            binding.submitButton.setText(R.string.register_button);
            binding.toggleMode.setText(R.string.toggle_to_login);
        }
        clearFields();
    }
    
    private void handleSubmit() {
        String username = binding.usernameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (isLoginMode) {
            handleLogin(username, password);
        } else {
            String email = binding.emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            handleRegister(username, email, password);
        }
    }
    
    private void handleLogin(String username, String password) {
        // Run in background thread
        new Thread(() -> {
            try {
                User user = database.userDao().getUserByUsername(username);
                runOnUiThread(() -> {
                    if (user != null && user.password.equals(password)) {
                        // Login successful
                        SharedPreferences prefs = getSharedPreferences("hw8_prefs", MODE_PRIVATE);
                        prefs.edit().putInt("current_user_id", user.id).apply();
                        prefs.edit().putString("current_username", user.username).apply();
                        
                        Toast.makeText(AuthActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AuthActivity.this, ChatActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AuthActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                    Toast.makeText(AuthActivity.this, "Login error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    
    private void handleRegister(String username, String email, String password) {
        // Run in background thread
        new Thread(() -> {
            try {
                User existingUser = database.userDao().getUserByUsername(username);
                User existingEmail = database.userDao().getUserByEmail(email);
                runOnUiThread(() -> {
                    if (existingUser != null) {
                        Toast.makeText(AuthActivity.this, R.string.username_exists, Toast.LENGTH_SHORT).show();
                    } else if (existingEmail != null) {
                        Toast.makeText(AuthActivity.this, R.string.email_exists, Toast.LENGTH_SHORT).show();
                    } else {
                        // Insert new user
                        new Thread(() -> {
                            try {
                                User newUser = new User(username, email, password);
                                database.userDao().insertUser(newUser);
                                User insertedUser = database.userDao().getUserByUsername(username);
                                
                                runOnUiThread(() -> {
                                    SharedPreferences prefs = getSharedPreferences("hw8_prefs", MODE_PRIVATE);
                                    prefs.edit().putInt("current_user_id", insertedUser.id).apply();
                                    prefs.edit().putString("current_username", insertedUser.username).apply();
                                    
                                    Toast.makeText(AuthActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AuthActivity.this, ChatActivity.class));
                                    finish();
                                });
                            } catch (Exception e) {
                                runOnUiThread(() ->
                                    Toast.makeText(AuthActivity.this, "Registration error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                            }
                        }).start();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                    Toast.makeText(AuthActivity.this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    
    private void clearFields() {
        binding.usernameInput.setText("");
        binding.passwordInput.setText("");
        binding.emailInput.setText("");
    }
}
