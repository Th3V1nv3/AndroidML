package com.facilesales.facilesalesapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface InvoiceWithSoldProductsDao {
    @Transaction
    @Query("SELECT * FROM Invoice")
    List<InvoiceWithSoldProducts> getInvoiceWithSoldProducts();
    @Query("SELECT * FROM Invoice ORDER BY invoiceId DESC LIMIT 1")
    InvoiceWithSoldProducts getLatest();
    @Insert
    long insertInvoice(Invoice invoice);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSoldProducts(List<SoldProducts> soldProducts);
}
