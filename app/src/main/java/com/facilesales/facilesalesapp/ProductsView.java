package com.facilesales.facilesalesapp;

import androidx.annotation.NonNull;
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

import com.facilesales.facilesalesapp.database.AvailProduct;
import com.facilesales.facilesalesapp.database.ProductViewModel;
import com.facilesales.facilesalesapp.pojos.ProductListAdapter;
import com.facilesales.facilesalesapp.pojos.RecyclerViewInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductsView extends AppCompatActivity implements RecyclerViewInterface {
    private ProductViewModel productViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    ProductListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ProductListAdapter(new ProductListAdapter.ProductDiff(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        productViewModel.getAllAvailProducts().observe(this, availProducts -> {
            // Update the cached copy of the availProducts in the adapter.
            adapter.submitList(availProducts);
        });

        adapter.notifyDataSetChanged();

        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ProductsView.this, Input_details .class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            AvailProduct availProduct = new AvailProduct();
            com.facilesales.facilesalesapp.pojos.AvailProduct av = (com.facilesales.facilesalesapp.pojos.AvailProduct) data.getSerializableExtra(ProductManager.EXTRA_REPLY);
            availProduct.setCost(av.getCost());
            availProduct.setSellingPrice(av.getSellingPrice());
            availProduct.setName(av.getName());
            availProduct.setQuantity(av.getQuantity());
            availProduct.setId(av.getId());

            productViewModel.insert(availProduct);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }


    public void showProductOption(int position)
    {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.product_sell_sheet);


        LinearLayout delete = (LinearLayout) bottomSheetDialog.findViewById(R.id.delete);
        LinearLayout addMore = (LinearLayout) bottomSheetDialog.findViewById(R.id.addMore);
        LinearLayout decrease = (LinearLayout) bottomSheetDialog.findViewById(R.id.subtract);
        LinearLayout update = (LinearLayout) bottomSheetDialog.findViewById(R.id.update);
        update.setVisibility(View.VISIBLE);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productViewModel.delete(adapter.getProductAt(position));
                bottomSheetDialog.dismiss();
            }
        });

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmIncrease(adapter.getProductAt(position));
                bottomSheetDialog.dismiss();
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDecrease(adapter.getProductAt(position));
                bottomSheetDialog.dismiss();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct(adapter.getProductAt(position));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void confirmIncrease(AvailProduct product)
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
                        Toast.makeText(getApplicationContext(), "Input is empty", Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(num) == 0) {
                        Toast.makeText(getApplicationContext(), "Input can not be zero", Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(num) < 0) {
                        Toast.makeText(getApplicationContext(), "Input can not be negative", Toast.LENGTH_LONG).show();
                    } else {
                        int value = product.getQuantity();
                        product.setQuantity(value + Integer.parseInt(num));
                        productViewModel.update(product);
                        Toast.makeText(getApplicationContext(), "Quantity increased by " + Integer.parseInt(num)+" to "+product.getQuantity() , Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                }
                catch (java.lang.NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"Input is empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void confirmDecrease(AvailProduct product)
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
                            int value = product.getQuantity();
                            if (value > Integer.parseInt(num)) {
                                product.setQuantity(value-(Integer.parseInt(num)));
                                productViewModel.update(product);
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

    public void updateProduct(AvailProduct availProduct)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.update_sheet);
        bottomSheetDialog.show();

        TextView id = (TextView)bottomSheetDialog.findViewById(R.id.p_id);
        EditText cost = (EditText)bottomSheetDialog.findViewById(R.id.updateCost);
        cost.setText(String.valueOf(availProduct.getCost()));
        EditText price = (EditText)bottomSheetDialog.findViewById(R.id.updatePrice);
        price.setText(String.valueOf(availProduct.getSellingPrice()));
        EditText quantity = (EditText)bottomSheetDialog.findViewById(R.id.updateQuantity);
        quantity.setText(String.valueOf(availProduct.getQuantity()));
        EditText name = (EditText)bottomSheetDialog.findViewById(R.id.updateName);
        name.setText(availProduct.getName());
        Button saveUpdate = (Button) bottomSheetDialog.findViewById(R.id.saveUpdate);
        id.setText(String.valueOf(availProduct.getId()));

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availProduct.setCost(Double.parseDouble(cost.getText().toString()));
                availProduct.setQuantity(Integer.parseInt(quantity.getText().toString()));
                availProduct.setSellingPrice(Double.parseDouble(price.getText().toString()));
                availProduct.setName(name.getText().toString());

                productViewModel.update(availProduct);
                Toast.makeText(getApplicationContext(),"Product updated",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        showProductOption(position);
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

