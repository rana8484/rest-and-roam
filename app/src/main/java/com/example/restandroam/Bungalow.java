package com.example.restandroam;

public class Bungalow {
    private int id;
    private String name;
    private String location;
    private double price;
    private boolean reserved;
    private String reservedDate;
    private int capacity;
    private int imgID;

    public Bungalow(String name, String location, double price, boolean reserved, String reservedDate, int capacity,int imgID) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.reserved = reserved;
        this.reservedDate = reservedDate;
        this.capacity = capacity;
        this.imgID = imgID;
    }

    public Bungalow(int id, String name, String location, double price, boolean reserved, String reservedDate, int capacity, int imgID) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.reserved = reserved;
        this.reservedDate = reservedDate;
        this.capacity = capacity;
        this.imgID = imgID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public boolean isReserved() {
        return reserved;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }
}
