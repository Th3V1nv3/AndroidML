package com.facilesales.facilesalesapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AvailProductsDao {
    @Insert
    void insertAll(AvailProducts... availProducts);

    @Delete
    void delete(AvailProducts availProducts);

    @Query("SELECT * FROM AvailProducts")
    List<AvailProducts> getAll();
}
