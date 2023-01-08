package com.facilesales.facilesalesapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Sold")
public class SoldProducts extends Products {
    public int invoiceId;
}
