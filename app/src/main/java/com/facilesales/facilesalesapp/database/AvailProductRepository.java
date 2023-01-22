package com.facilesales.facilesalesapp.database;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.facilesales.facilesalesapp.database.*;

import java.util.List;

public class Repository {

   /* private InvoiceDao invoiceDao;
    private SoldProductsDao soldProductsDao;
    private InvoiceWithSoldProductsDao invoiceWithSoldProductsDao;*/
    private AvailProductsDao availProductsDao;
    private LiveData<List<AvailProducts>> allAvailProducts;

    public Repository(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        availProductsDao = db.availProductsDao();
        allAvailProducts = availProductsDao.getAll();

    }

    public LiveData<List<AvailProducts>> getAllAvailProducts() {
        return allAvailProducts;
    }

    public void insert(AvailProducts availProducts){
        availProductsDao.insert(availProducts);
    }
}
