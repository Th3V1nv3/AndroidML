package com.facilesales.facilesalesapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CashFlowDao {

    @Update
    void update(CashFlow cashFlow);

    @Delete
    void delete(CashFlow cashFlow);

    @Insert
    void insert(CashFlow cashFlow);

    @Query("SELECT * FROM CashFlow")
    CashFlow getCashFlow();
}
