package com.example.a1tapevents.models;

public class BookingModel {
    String username,email,date,time,status,location;
    long contact;

    public BookingModel() {
    }

    public BookingModel(String username, String email, long contact, String date, String time, String status, String location) {
        this.username = username;
        this.email = email;
        this.contact = contact;
        this.date = date;
        this.time = time;
        this.status = status;
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
