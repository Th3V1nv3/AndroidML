package com.facilesales.facilesalesapp.database;
import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AvailProductRepository {

   /* private InvoiceDao invoiceDao;
    private SoldProductsDao soldProductsDao;*/
    private InvoiceWithSoldProductsDao invoiceWithSoldProductsDao;
    private AvailProductsDao availProductsDao;
    private LiveData<List<AvailProduct>> allAvailProducts;
    private List<InvoiceWithSoldProducts> allInvoiceWithSoldProducts;
    private CashFlowDao cashFlowDao;


    public AvailProductRepository(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        availProductsDao = db.availProductsDao();
        invoiceWithSoldProductsDao = db.invoiceWithSoldProductsDao();
        allInvoiceWithSoldProducts = invoiceWithSoldProductsDao.getInvoiceWithSoldProducts();
        allAvailProducts = availProductsDao.getAll();
    }

    public LiveData<List<AvailProduct>> getAllAvailProducts() {
        return allAvailProducts;
    }

    public List<InvoiceWithSoldProducts> getAllInvoiceWithSoldProducts(){
        return allInvoiceWithSoldProducts;
    }

    public void insert(AvailProduct availProduct){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            availProductsDao.insert(availProduct);
        });
    }
    public void delete(AvailProduct availProduct){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            availProductsDao.delete(availProduct);
        });
    }
    public void update(AvailProduct availProduct){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            availProductsDao.update(availProduct);
        });
    }
    public InvoiceWithSoldProducts getLastest(){
        return invoiceWithSoldProductsDao.getLatest();
    }

    public AvailProduct isAvailable(int id){
        AvailProduct availProduct = availProductsDao.getProduct(id);

        return availProduct;
    }

    public void insertFinalInvoice(Invoice invoice,List<SoldProducts> soldProducts){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            insertHelper(invoice,soldProducts);
        });
    }
    private void insertHelper(Invoice invoice,List<SoldProducts> soldProductsList)
    {
        int id = (int)invoiceWithSoldProductsDao.insertInvoice(invoice);

        for (SoldProducts sp: soldProductsList) {
            sp.setInvoiceId(id);
        }

        invoiceWithSoldProductsDao.insertSoldProducts(soldProductsList);
    }

}
