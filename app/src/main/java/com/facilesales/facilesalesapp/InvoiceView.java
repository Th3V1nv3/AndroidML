package com.facilesales.facilesalesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InvoiceView extends AppCompatActivity {
    private FloatingActionButton fab;
    private String soldInfo;
    private String invoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_view);
        fab = (FloatingActionButton) findViewById(R.id.fab_email);
        TextView inv = findViewById(R.id.invoice_id);
        TextView sold = findViewById(R.id.productsSold);

        Intent intent = getIntent();
        invoice = intent.getStringExtra("invoice");
        soldInfo = intent.getStringExtra("sold");
        inv.setText(invoice);
        sold.setText(soldInfo);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvoiceEmails(invoice,soldInfo);
            }
        });
    }

    public void sendInvoiceEmails(String invoice, String soldInfo)
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, "Invoice from Facile Sales");
        email.putExtra(Intent.EXTRA_TEXT,invoice+"\n\n"+soldInfo);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}