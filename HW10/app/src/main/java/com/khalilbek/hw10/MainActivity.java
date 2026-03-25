package com.khalilbek.hw10;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.khalilbek.hw10.adapter.ImageAdapter;
import com.khalilbek.hw10.databinding.ActivityMainBinding;
import com.khalilbek.hw10.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new ImageAdapter(createImages()));
    }

    private List<ImageItem> createImages() {
        List<ImageItem> images = new ArrayList<>();
        images.add(new ImageItem("Mountain lake", "https://picsum.photos/id/10/800/500"));
        images.add(new ImageItem("Forest road", "https://picsum.photos/id/20/800/500"));
        images.add(new ImageItem("Sunny beach", "https://picsum.photos/id/30/800/500"));
        images.add(new ImageItem("City lights", "https://picsum.photos/id/40/800/500"));
        images.add(new ImageItem("Desert dunes", "https://picsum.photos/id/50/800/500"));
        images.add(new ImageItem("River valley", "https://picsum.photos/id/60/800/500"));
        images.add(new ImageItem("Snowy peaks", "https://picsum.photos/id/70/800/500"));
        images.add(new ImageItem("Countryside", "https://picsum.photos/id/80/800/500"));
        images.add(new ImageItem("Morning fog", "https://picsum.photos/id/90/800/500"));
        images.add(new ImageItem("Autumn trail", "https://picsum.photos/id/100/800/500"));
        return images;
    }
}
