package com.facilesales.facilesalesapp.database;

import androidx.room.*;

import java.util.Date;

@Entity(tableName = "Invoice")
public class Invoice {
    @PrimaryKey(autoGenerate = true)
    public int invoiceId;
    public Date date;

}
