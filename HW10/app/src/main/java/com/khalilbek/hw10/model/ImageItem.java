package com.khalilbek.hw10.model;

public class ImageItem {
    private final String title;
    private final String imageUrl;

    public ImageItem(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
