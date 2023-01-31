package com.facilesales.facilesalesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facilesales.facilesalesapp.database.Invoice;
import com.facilesales.facilesalesapp.database.InvoiceWithSoldProducts;
import com.facilesales.facilesalesapp.database.ProductViewModel;
import com.facilesales.facilesalesapp.database.SoldProducts;
import com.facilesales.facilesalesapp.pojos.AvailProduct;
import com.facilesales.facilesalesapp.pojos.RecyclerViewInterface;
import com.facilesales.facilesalesapp.pojos.SellingViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SellingView extends AppCompatActivity implements RecyclerViewInterface {
    private FloatingActionButton fab;
    private final int REQUEST_CODE_CAM = 1;
    private final String CAM_EXTRA_NAME = "com.facilesales.facilesalesapp.CAM";
    RecyclerView recyclerView;
    List<AvailProduct> productList;
    SellingViewAdapter adapter;
    FloatingActionButton done;
    ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_view2);

        recyclerView = findViewById(R.id.recyclerview_selling);
        fab = (FloatingActionButton)findViewById(R.id.forward);
        done = (FloatingActionButton)findViewById(R.id.done);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productList = new ArrayList<>();

        adapter = new SellingViewAdapter(productList,this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(view -> {
            Intent detailsIntent = new Intent(this,Cam.class);
            startActivityForResult(detailsIntent,REQUEST_CODE_CAM);
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewDialog();
            }
        });
    }

    public void showAddNewDialog()
    {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.payment_sheet);


        TextView amountDue = (TextView) bottomSheetDialog.findViewById(R.id.amount_due);
        EditText paid = (EditText)bottomSheetDialog.findViewById(R.id.amount_paid);
        TextView change = (TextView) bottomSheetDialog.findViewById(R.id.change);
        Button getChange = (Button) bottomSheetDialog.findViewById(R.id.get_change);
        Button save = (Button) bottomSheetDialog.findViewById(R.id.save);
        save.setVisibility(View.GONE);
        double amount = 0.0;

        for (AvailProduct product:productList) {
            amount = amount + (product.getSellingPrice()*product.getQuantity());
        }
        amountDue.setText("Pay : R"+new DecimalFormat("##.##").format(amount));


        double finalAmount = amount;
        getChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Double.parseDouble(paid.getText().toString()) >= finalAmount) {
                        double changeValue = Double.parseDouble(paid.getText().toString()) - finalAmount;
                        change.setText("Change : R" + String.valueOf(changeValue));
                        save.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Payment less than amount due", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Payment is empty",Toast.LENGTH_SHORT).show();
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                saveInvoice();
                updateSoldProducts();
                populateInvoiceString();
                // this line closes the dialog
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(REQUEST_CODE_CAM == requestCode && resultCode == RESULT_OK)
        {
            boolean flag = true;

            AvailProduct av = (AvailProduct)data.getSerializableExtra(CAM_EXTRA_NAME);
            for (int i = 0;i < productList.size();i++) {
                AvailProduct avp = productList.get(i);
                if(avp.getId() == av.getId()){
                    flag = false;
                    int q = avp.getQuantity() + 1;
                    productList.get(i).setQuantity(q);
                    adapter.notifyItemChanged(i);
                }
            }
            if(flag){
                productList.add(av);
                adapter.notifyItemInserted(productList.size()-1);
            }
            done.setVisibility(View.VISIBLE);

        }
    }
    public void showProductOption(int position)
    {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.product_sell_sheet);


        LinearLayout delete = (LinearLayout) bottomSheetDialog.findViewById(R.id.delete);
        LinearLayout addMore = (LinearLayout) bottomSheetDialog.findViewById(R.id.addMore);
        LinearLayout decrease = (LinearLayout) bottomSheetDialog.findViewById(R.id.subtract);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productList.remove(position);
                adapter.notifyItemRemoved(position);
                bottomSheetDialog.dismiss();
            }
        });

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmIncrease(position);
                bottomSheetDialog.dismiss();
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDecrease(position);
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();
    }

    private void confirmIncrease(int position)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.confirm_increase);

        EditText editText = (EditText) bottomSheetDialog.findViewById(R.id.num);
        Button button = bottomSheetDialog.findViewById(R.id.save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String num = editText.getText().toString();
                    if (num == null) {
                        Toast.makeText(getApplicationContext(), "Input is empty", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(num) == 0) {
                        Toast.makeText(getApplicationContext(), "Input can not be zero", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(num) < 0) {
                        Toast.makeText(getApplicationContext(), "Input can not be negative", Toast.LENGTH_SHORT).show();
                    } else {
                        int value = productList.get(position).getQuantity();
                        int holderValue = productList.get(position).getQuantityHolder();
                        if(holderValue >= Integer.parseInt(num)){
                            productList.get(position).setQuantity(value + Integer.parseInt(num));
                            adapter.notifyItemChanged(position);
                            Toast.makeText(getApplicationContext(),   "Quantity increased by " + Integer.parseInt(num) + " to " + productList.get(position).getQuantity(), Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "You only have "+holderValue+" items in stock", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (java.lang.NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"Input is empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        bottomSheetDialog.show();
    }
    private void confirmDecrease(int position)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.confirm_increase);

        EditText editText = (EditText) bottomSheetDialog.findViewById(R.id.num);
        Button button = bottomSheetDialog.findViewById(R.id.save);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (editText.getText() != null) {
                        String num = editText.getText().toString();
                        if (num == null) {
                            Toast.makeText(getApplicationContext(), "Input is empty", Toast.LENGTH_LONG).show();
                        } else if (Integer.parseInt(num) == 0) {
                            Toast.makeText(getApplicationContext(), "Input can not be zero", Toast.LENGTH_LONG).show();
                        } else if (Integer.parseInt(num) < 0) {
                            Toast.makeText(getApplicationContext(), "Input can not be negative", Toast.LENGTH_LONG).show();
                        } else {
                            int value = productList.get(position).getQuantity();
                            if (value > Integer.parseInt(num)) {
                                productList.get(position).setQuantity(value - Integer.parseInt(num));
                                adapter.notifyItemChanged(position);
                                Toast.makeText(getApplicationContext(), "Quantity decreased by " + Integer.parseInt(num), Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Decreasing value is less or equal number of products", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Input is empty", Toast.LENGTH_LONG).show();
                    }
                }catch (java.lang.NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"Input is empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        bottomSheetDialog.show();
    }
    public void saveInvoice()
    {
        Invoice invoice = new Invoice();
        invoice.date = new Date();

        viewModel.insertFinalInvoice(invoice,getSoldProducts());

    }
    private List<SoldProducts> getSoldProducts(){
        List<SoldProducts> allSoldProducts = new ArrayList<>();
        SoldProducts soldProducts;
        for ( AvailProduct av: productList) {
            soldProducts = new SoldProducts();
            soldProducts.setId(av.getId());
            soldProducts.setName(av.getName());
            soldProducts.setQuantity(av.getQuantity());
            soldProducts.setSellingPrice(av.getSellingPrice());
            soldProducts.setCost(av.getCost());

            allSoldProducts.add(soldProducts);
        }
        return allSoldProducts;
    }
    @Override
    public void onItemClick(int position) {
        showProductOption(position);
    }

    public void updateSoldProducts()
    {
        for (AvailProduct ap:productList) {
            com.facilesales.facilesalesapp.database.AvailProduct current = viewModel.getProduct(ap.getId());
            current.setQuantity(current.getQuantity()-ap.getQuantity());
            viewModel.update(current);
        }
    }

    public void populateInvoiceString()
    {
        Intent intent = new Intent(getApplicationContext(),InvoiceView.class);
        InvoiceWithSoldProducts iws = viewModel.getLatest();
        String inv = "Invoice ID : "+iws.getInvoice().invoiceId+" \nDate :"+iws.getInvoice().date.toString();
        double amountDue = 0.0;
        String allSold = "List of all the products\n" +
                "-----------------------------\n";
        for (AvailProduct sp: productList) {
            amountDue = amountDue + sp.getSellingPrice();
            allSold = allSold +"\nProduct ID : "+ sp.getId()+"\nName : "+sp.getName()+
                    "\nSelling price : R"+sp.getSellingPrice()+"\nNo. Items sold : "+sp.getQuantity()+"\n" +
                    "-----------------------------";
        }
        allSold = allSold + "------------------------------\nTotal amount : R"+amountDue;
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