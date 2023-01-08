package com.facilesales.facilesalesapp.database;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface InvoiceWithSoldProductsDao {
    @Transaction
    @Query("SELECT * FROM Invoice")
    public List<InvoiceWithSoldProducts> getUsersWithPlaylists();
}
