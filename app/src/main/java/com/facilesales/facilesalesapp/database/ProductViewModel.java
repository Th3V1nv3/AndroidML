package com.facilesales.facilesalesapp.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private AvailProductRepository availProductRepository;
    private LiveData<List<AvailProduct>> allAvailProducts;
    private List<InvoiceWithSoldProducts> allInvoiceWithSoldProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        availProductRepository = new AvailProductRepository(application);
        allAvailProducts = availProductRepository.getAllAvailProducts();
        allInvoiceWithSoldProducts = availProductRepository.getAllInvoiceWithSoldProducts();
    }

    public LiveData<List<AvailProduct>> getAllAvailProducts() {
        return allAvailProducts;
    }
    public List<InvoiceWithSoldProducts> getAllInvoiceWithSoldProducts(){return allInvoiceWithSoldProducts;}
    public void insert(AvailProduct availProduct){
        availProductRepository.insert(availProduct);
    }
    public void delete(AvailProduct availProduct){
        availProductRepository.delete(availProduct);
    }
    public void update(AvailProduct availProduct){
        availProductRepository.update(availProduct);
    }
    public AvailProduct getProduct(int id){ return availProductRepository.isAvailable(id);}
    public InvoiceWithSoldProducts getLatest(){
        return availProductRepository.getLastest();
    }

    public void insertFinalInvoice(Invoice invoice,List<SoldProducts> soldProducts)
    {
        availProductRepository.insertFinalInvoice(invoice,soldProducts);
    }

}
