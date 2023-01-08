package com.facilesales.facilesalesapp.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class InvoiceWithSoldProducts
{
    @Embedded
    public Invoice invoice;
    @Relation(parentColumn = "invoiceId",entityColumn = "invoiceId")
    public List<SoldProducts> soldProducts;
}
