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
        images.add(new ImageItem("Mountains", "https://upload.wikimedia.org/wikipedia/commons/3/3f/Fronalpstock_big.jpg"));
        images.add(new ImageItem("Forest", "https://upload.wikimedia.org/wikipedia/commons/a/a5/Redwood_forest.jpg"));
        images.add(new ImageItem("Beach", "https://upload.wikimedia.org/wikipedia/commons/6/6e/Golde33443.jpg"));
        images.add(new ImageItem("River", "https://upload.wikimedia.org/wikipedia/commons/2/24/Wilson_River%2C_Oregon.jpg"));
        images.add(new ImageItem("Waterfall", "https://upload.wikimedia.org/wikipedia/commons/9/9e/Lone_Creek_Falls2.jpg"));
        images.add(new ImageItem("Lake", "https://upload.wikimedia.org/wikipedia/commons/0/0e/Lake_mapourika_NZ.jpeg"));
        images.add(new ImageItem("Road", "https://upload.wikimedia.org/wikipedia/commons/8/88/Blue_Ridge_Parkway_in_Fall.jpg"));
        images.add(new ImageItem("Desert", "https://upload.wikimedia.org/wikipedia/commons/8/87/Sossusvlei_Dune.jpg"));
        images.add(new ImageItem("Snow", "https://upload.wikimedia.org/wikipedia/commons/1/12/Matterhorn_from_Domh%C3%BCtte_-_2.jpg"));
        images.add(new ImageItem("City", "https://upload.wikimedia.org/wikipedia/commons/e/e6/New_York_City_at_night_HDR.jpg"));
        return images;
    }
}
