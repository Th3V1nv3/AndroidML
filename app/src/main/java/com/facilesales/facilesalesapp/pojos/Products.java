package com.facilesales.facilesalesapp.pojos;

import java.io.Serializable;

public abstract class Products implements Serializable {
    public int id;
    public String name;
    public double cost;
    public double sellingPrice;
    public int quantity;

    public Products() {
    }

    public Products(int id, String name, double cost, double sellingPrice, int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
