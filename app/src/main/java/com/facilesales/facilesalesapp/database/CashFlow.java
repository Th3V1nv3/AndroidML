package com.facilesales.facilesalesapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CashFlow {
    @PrimaryKey
    int id;
    @ColumnInfo(defaultValue = "0")
    double totalSales;
    @ColumnInfo(defaultValue = "0")
    double totalCost;
}
