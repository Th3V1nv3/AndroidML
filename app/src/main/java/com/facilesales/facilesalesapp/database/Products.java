package com.facilesales.facilesalesapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public abstract class Products {
    @PrimaryKey
    public int id;
    public String name;
    public double cost;
    public double sellingPrice;
    public String quantity;
}
