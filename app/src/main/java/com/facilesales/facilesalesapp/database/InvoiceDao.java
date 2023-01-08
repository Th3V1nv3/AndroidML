package com.facilesales.facilesalesapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InvoiceDao {
    @Insert
    void insertAll(Invoice... invoices);

    @Delete
    void delete(Invoice invoice);

    @Query("SELECT * FROM Invoice")
    List<Invoice> getAll();
}
