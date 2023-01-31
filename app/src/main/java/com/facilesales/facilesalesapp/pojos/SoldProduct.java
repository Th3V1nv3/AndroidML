package com.facilesales.facilesalesapp.pojos;

public class SoldProduct extends Products {
    public int invoiceId;

    public SoldProduct(int id, String name, double cost, double sellingPrice, int quantity) {
        super(id, name, cost, sellingPrice, quantity);
    }
}
