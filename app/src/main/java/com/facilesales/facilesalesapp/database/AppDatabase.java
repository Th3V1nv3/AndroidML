package com.facilesales.facilesalesapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Invoice.class,SoldProducts.class,AvailProducts.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AvailProductsDao availProductsDao();
    public abstract InvoiceDao invoiceDao();
    public abstract InvoiceWithSoldProductsDao invoiceWithSoldProductsDao();
    public abstract SoldProductsDao soldProductsDao();

}