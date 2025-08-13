package com.example.demo.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@Document("items")
public class Item {
    @Id
    private String id;
    private String name;
    private String supplier;
    private int price;
    private int quantity;
    public LocalDateTime lastUpdated;

    public Item(String name, String supplier, int price, int quantity)
    {
        this.name = name;
        this.supplier = supplier;
        this.price = price;
        this.quantity = quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getLastUpdated(){return this.lastUpdated.toString();}

    public String getSupplier() {
        return supplier;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
    public String getId() {
        return id;
    }
    public String toString()
    {
        return "name: "+this.name+"\nprice: "+this.price+"\nquantity: "+this.quantity+"\nsupplier: "+supplier+"\nid: "+this.id;}
}
