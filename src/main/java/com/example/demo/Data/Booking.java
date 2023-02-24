package com.example.demo.Data;
import com.example.demo.Utility.GetTimeStamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//@Entity
@Document("bookings")
public class Booking {

//    @GeneratedValue
    @Id
    private String id;
    private String username;
    private String barberUsername;
    private String openingId;
    public String openingInfo;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getBarberUsername() {
        return barberUsername;
    }

    public String getOpeningId() {
        return openingId;
    }

    public int getPrice() {
        return price;
    }

    private  int price;

//    public Booking(String barberUsername, String username,String openingId,int price) {
//        this.username = username;
//        this.barberUsername = barberUsername;
//        this.openingId = openingId;
//        this.price = price;
//
//    }
    public Booking(String barberUsername, String username,String openingId,int price,String openingInfo)
    {
        this.username = username;
        this.barberUsername = barberUsername;
        this.openingId = openingId;
        this.price = price;
        this.openingInfo = openingInfo;
    }




    public Booking(){};





}
