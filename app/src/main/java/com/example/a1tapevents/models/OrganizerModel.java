package com.example.a1tapevents.models;

public class OrganizerModel {
    String name,about,address,contact,email;

    public OrganizerModel() {
    }

    public OrganizerModel(String name, String about, String address, String contact, String email) {
        this.name = name;
        this.about = about;
        this.address = address;
        this.contact = contact;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddrress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
