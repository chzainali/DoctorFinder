package com.dev.doctorfinder.home.model;

public class PagerModel {
    int imageUrl;

    public PagerModel() {
    }

    public PagerModel(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}
