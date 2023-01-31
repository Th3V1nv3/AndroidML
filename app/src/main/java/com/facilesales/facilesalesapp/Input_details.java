package com.facilesales.facilesalesapp;

import static com.facilesales.facilesalesapp.ProductManager.EXTRA_REPLY;
import static com.facilesales.facilesalesapp.ProductManager.INPUT_REPLY;
import static com.facilesales.facilesalesapp.ProductsView.NEW_WORD_ACTIVITY_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facilesales.facilesalesapp.database.ProductViewModel;
import com.facilesales.facilesalesapp.pojos.AvailProduct;
import com.facilesales.facilesalesapp.pojos.Products;

public class Input_details extends AppCompatActivity {
    private AvailProduct availProduct;
    private Button id;
    private ProductViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_details);
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        availProduct = new AvailProduct();

        id = (Button)findViewById(R.id.enterid);
        EditText name = (EditText)findViewById(R.id.enterName);
        EditText cost = (EditText)findViewById(R.id.enterCost);
        EditText price = (EditText)findViewById(R.id.enterPrice);
        EditText quantity = (EditText)findViewById(R.id.enterQuantity);
        Button save = (Button)findViewById(R.id.save);
        Intent cam = new Intent(getApplicationContext(),ProductManager.class);

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(cam, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if(id.getText().toString().equals("Click here first"))
                    {
                        Toast.makeText(getApplicationContext(),"Click the 'Click here first' button",Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.parseInt(quantity.getText().toString()) == 0) {
                        Toast.makeText(getApplicationContext(), "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(quantity.getText().toString()) < 0) {
                        Toast.makeText(getApplicationContext(), "Quantity cannot be less than 0", Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(cost.getText().toString()) <= 0) {
                        Toast.makeText(getApplicationContext(), "Item must cost R1 or more", Toast.LENGTH_SHORT).show();
                    }else if (Double.parseDouble(price.getText().toString()) <= 0) {
                        Toast.makeText(getApplicationContext(), "Item must sell for R1 or more", Toast.LENGTH_SHORT).show();
                    }else if (Double.parseDouble(price.getText().toString()) <= Double.parseDouble(cost.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Selling price must more than cost price", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        availProduct.setName(name.getText().toString());
                        availProduct.setId(Integer.parseInt(id.getText().toString()));
                        availProduct.setQuantity(Integer.parseInt(quantity.getText().toString()));
                        availProduct.setCost(Double.parseDouble(cost.getText().toString()));
                        availProduct.setSellingPrice(Double.parseDouble(price.getText().toString()));

                        Intent replyIntent = new Intent();
                        replyIntent.putExtra(EXTRA_REPLY, availProduct);
                        setResult(RESULT_OK, replyIntent);

                        finish();
                    }

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Fill all the spaces",Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String myID = data.getStringExtra(INPUT_REPLY);
        if(viewModel.getProduct(Integer.parseInt(myID)) != null)
        {
            Toast.makeText(getApplicationContext(),"Product exists",Toast.LENGTH_SHORT).show();

        }
        else {
            id.setText(myID);
        }

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