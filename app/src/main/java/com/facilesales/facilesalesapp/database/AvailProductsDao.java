package com.facilesales.facilesalesapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AvailProductsDao {
    @Insert
    void insert(AvailProduct availProduct);

    @Delete
    void delete(AvailProduct availProduct);

    @Query("SELECT * FROM AvailProduct")
    LiveData<List<AvailProduct>> getAll();

    @Query("SELECT * FROM AvailProduct WHERE id = :myId")
    AvailProduct getProduct(int myId);

    @Update
    void update(AvailProduct availProduct);


}
