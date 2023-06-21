package com.example.tictacjournalofficial.entities;

public class ColorCount {

    private String color;
    private Integer count;

    // default constructor
    public ColorCount() {
    }
    // Constructor
    public ColorCount(String color, Integer count) {
        this.color = color;
        this.count = count;
    }

    // Getter and setter methods
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

