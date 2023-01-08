package com.facilesales.facilesalesapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SoldProductsDao {
    @Insert
    void insertAll(SoldProducts... soldProducts);

    @Delete
    void delete(SoldProducts soldProducts);

    @Query("SELECT * FROM Sold")
    List<SoldProducts> getAll();
}
