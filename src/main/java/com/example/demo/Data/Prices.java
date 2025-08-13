package com.example.demo.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document(collection = "prices")
public class Prices {
    @Id
    private String id;
    private  int menHairCutPrice;
    private  int womanHairCutPrice;
    private  int womanHairDyePrice;
    private LocalDateTime lastUpdated;

    public Prices(int menHairCutPrice,int womanHairCutPrice,int womanHairDyePrice)
    {

        this.menHairCutPrice = menHairCutPrice;
        this.womanHairCutPrice = womanHairCutPrice;
        this.womanHairDyePrice = womanHairDyePrice;
        this.lastUpdated = LocalDateTime.now();

    }
    public String getId(){return id;}

    public int getwomanHairDyePrice(){return womanHairDyePrice;}
    public int getwomanHairCutPrice(){return womanHairCutPrice;}
    public int getmenHairCutPrice(){return menHairCutPrice;}

    public void setmenHairCutPrice(int newPrice){menHairCutPrice = newPrice;}
    public void setwomanHairCutPrice(int newPrice){womanHairCutPrice = newPrice;}
    public void setWomanHairDyePrice(int newPrice){womanHairDyePrice = newPrice;}

    public LocalDateTime getLastUpdated(){return lastUpdated;}
    public void setLastUpdated(LocalDateTime lastUpdated){this.lastUpdated = lastUpdated;}

    public String toString(){
        return "\nmen hair cut prices: "+menHairCutPrice+"\nwoman hair cut prices: "+womanHairCutPrice+"\nwoman hair dye prices: "+womanHairDyePrice;
    }
}
