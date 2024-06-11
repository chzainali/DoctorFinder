package com.dev.doctorfinder.admin.home.model;

import java.io.Serializable;

public class ShopModel implements Serializable {
    int price;
    String id,title,details,imageUri;

    public ShopModel(int price, String id, String title, String details, String imageUri) {
        this.price = price;
        this.id = id;
        this.title = title;
        this.details = details;
        this.imageUri = imageUri;
    }
    public ShopModel(){

    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
