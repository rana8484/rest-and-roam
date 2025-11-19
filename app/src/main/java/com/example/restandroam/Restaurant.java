package com.example.restandroam;

public class Restaurant {
    private int id;
    private String name;
    private String location;
    private String openingHours;

    public Restaurant(String name, String location, String openingHours) {
        this.name = name;
        this.location = location;
        this.openingHours = openingHours;
    }

    public Restaurant(int id, String name, String location, String openingHours) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.openingHours = openingHours;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getOpeningHours() {
        return openingHours;
    }
}
