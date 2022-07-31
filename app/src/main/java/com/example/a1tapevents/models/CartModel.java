package com.example.a1tapevents.models;

public class CartModel {
    private String organizer,contact,date,time;
    long price;

    public CartModel() {
    }

    public CartModel(String organizer, String contact, String date, String time, long price) {
        this.organizer = organizer;
        this.contact = contact;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public String getOrganizer() {
        return organizer;
    }

    public long getPrice() {
        return price;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
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

}
