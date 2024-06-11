package com.dev.doctorfinder.model;

import java.io.Serializable;

public class DoctorsModel implements Serializable {
    String id,name, details,date,stars,image,experts,email, location;

    public DoctorsModel(String id, String name, String details, String date, String stars, String image, String experts, String email, String location) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.date = date;
        this.stars = stars;
        this.image = image;
        this.experts = experts;
        this.email = email;
        this.location = location;
    }

    public DoctorsModel(){

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExperts() {
        return experts;
    }

    public void setExperts(String experts) {
        this.experts = experts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
