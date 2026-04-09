package com.example.mototrip;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // Good practice to avoid USER keyword issues
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private boolean premium;
    private int points;

    public User() {
    }

    public User(String name, boolean premium) {
        this.name = name;
        this.premium = premium;
        this.points = 0;
    }

    public void addPoints(int pts) {
        this.points += pts;
    }

    public boolean canJoinPremium() {
        return premium;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
