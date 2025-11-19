package com.example.restandroam;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private double budget;

    public User(int id, String username, String password, String email, double budget) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public double getBudget() {
        return budget;
    }

    public boolean canAfford(double amount) {
        return budget >= amount;
    }

    public void deductBudget(double amount) {
        if (amount > budget) {
            throw new IllegalArgumentException("Insufficient budget.");
        }
        budget -= amount;
    }
}
