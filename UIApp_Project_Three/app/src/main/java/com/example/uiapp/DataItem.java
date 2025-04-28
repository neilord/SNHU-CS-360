package com.example.uiapp;

/** Simple model class for inventory rows. */
public class DataItem {
    private long   id;
    private String title;
    private String details;
    private int    qty;

    public DataItem(long id, String title, String details, int qty) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.qty = qty;
    }

    public long   getId()      { return id; }
    public String getTitle()   { return title; }
    public String getDetails() { return details; }
    public int    getQty()     { return qty; }
}
