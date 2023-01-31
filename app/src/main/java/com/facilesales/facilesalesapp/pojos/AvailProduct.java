package com.facilesales.facilesalesapp.pojos;

public class AvailProduct extends Products{

    public AvailProduct() {
    }

    private int quantityHolder;

    public AvailProduct(int id, String name, double cost, double sellingPrice, int quantity,int quantityHolder) {
        super(id, name, cost, sellingPrice, quantity);
        this.quantityHolder = quantityHolder;
    }

    public int getQuantityHolder() {
        return quantityHolder;
    }

    public void setQuantityHolder(int quantityHolder) {
        this.quantityHolder = quantityHolder;
    }
}
