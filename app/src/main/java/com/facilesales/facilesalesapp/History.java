package com.facilesales.facilesalesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facilesales.facilesalesapp.database.InvoiceWithSoldProducts;
import com.facilesales.facilesalesapp.database.ProductViewModel;
import com.facilesales.facilesalesapp.database.SoldProducts;
import com.facilesales.facilesalesapp.pojos.HistoryRow;
import com.facilesales.facilesalesapp.pojos.HistoryViewAdapter;
import com.facilesales.facilesalesapp.pojos.ProductListAdapter;
import com.facilesales.facilesalesapp.pojos.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements RecyclerViewInterface {
    private List<HistoryRow> historyRowList;
    private ProductViewModel viewModel;
    private List<InvoiceWithSoldProducts> invoiceWithSoldProducts;
    private HistoryViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        invoiceWithSoldProducts = viewModel.getAllInvoiceWithSoldProducts();
        historyRowList = new ArrayList<>();
        populateHistoryList();

        RecyclerView recyclerView = findViewById(R.id.recyclerview_history);
        adapter = new HistoryViewAdapter(historyRowList,this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void populateHistoryList()
    {
        for (InvoiceWithSoldProducts iws:invoiceWithSoldProducts) {
            historyRowList.add(new HistoryRow(iws.getInvoice().invoiceId,iws.getInvoice().date,getTotalAmountDue(iws.getSoldProducts())));
        }

    }
    public double getTotalAmountDue(List<SoldProducts> soldProducts){
        double amount = 0.0;
        for (SoldProducts product:soldProducts) {
            amount = amount + (product.getSellingPrice()*product.getQuantity());
        }
        return amount;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(),InvoiceView.class);
        InvoiceWithSoldProducts iws = invoiceWithSoldProducts.get(position);
        String inv = "Invoice ID : "+iws.getInvoice().invoiceId+" \nDate :"+iws.getInvoice().date.toString();
        String allSold = "List of all the products\n" +
                        "-----------------------------\n";
        List<SoldProducts> soldProductsList = iws.getSoldProducts();
        double amountDue = 0.0;
        for (SoldProducts sp: soldProductsList) {
            amountDue = amountDue + sp.getSellingPrice();
            allSold = allSold +"\nProduct ID : "+ sp.getId()+"\nName : "+sp.getName()+
                    "\nSelling price : R"+sp.getSellingPrice()+"\nNo. Items sold : "+sp.getQuantity()+"\n" +
                        "-----------------------------";
        }
        allSold = allSold + "\n------------------------------\nTotal amount : R"+amountDue;
        intent.putExtra("invoice",inv);
        intent.putExtra("sold",allSold);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}