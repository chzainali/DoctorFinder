package com.dev.doctorfinder.home.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingModel implements Serializable {

    String id,userId,doctorId,date,doctorName,title,status,details,email;

    public BookingModel() {

    }

    public BookingModel(String id, String userId, String doctorId, String date, String doctorName, String title,String status,String details,String email) {
        this.id = id;
        this.userId = userId;
        this.doctorId = doctorId;
        this.date = date;
        this.doctorName = doctorName;
        this.title = title;
        this.status=status;
        this.details=details;
        this.email=email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String formatedTime(){
        String formatedDate="";
        Date date1=new Date(Long.parseLong(getDate()));
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MMMMM/yyyy hh:mm");
        formatedDate= sdf.format(date1);
        return formatedDate;
    }
}
