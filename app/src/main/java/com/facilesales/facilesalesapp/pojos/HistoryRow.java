package com.facilesales.facilesalesapp.pojos;

import java.util.Date;

public class HistoryRow {
    private int invoiceId;
    private Date date;
    private double totalMountMade;

    public HistoryRow() {
    }

    public HistoryRow(int invoiceId, Date date, double totalMountMade) {
        this.invoiceId = invoiceId;
        this.date = date;
        this.totalMountMade = totalMountMade;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalMountMade() {
        return totalMountMade;
    }

    public void setTotalMountMade(double totalMountMade) {
        this.totalMountMade = totalMountMade;
    }
}
