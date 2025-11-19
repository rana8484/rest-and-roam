package com.example.restandroam;

public class Activity {
    private int id;
    private String name;
    private String location;
    private double price;

    public Activity(String name, String location, double price) {
        this.name = name;
        this.location = location;
        this.price = price;
    }

    public Activity(int id, String name, String location, double price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

