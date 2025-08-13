package com.example.demo.Data;
import com.example.demo.Utility.GetTimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
@Document("bookings")
public class Booking {
    private static final Logger log = LoggerFactory.getLogger(Booking.class); // אובייקט מסוג תור לא פנוי

    //    @GeneratedValue
    @Id
    protected String id;

    protected String username;
    protected String barberUsername;
    protected String openingId;
    protected String opening2Id = null;
    public String openingInfo;

    protected String phoneNumber;
    int price;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

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

    public String get2ndOpeningID() {
        return opening2Id;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price){this.price = price;}

    public Booking(String barberUsername, String username, String openingId, int price, String openingInfo, LocalDateTime startTime, LocalDateTime endTime) // בנאי
    {
        this.username = username;
        this.barberUsername = barberUsername;
        this.openingId = openingId;
        this.openingInfo = openingInfo;
        this.startTime = startTime;
        this.endTime = endTime;
        System.out.println("startTime for new booking: " + this.startTime);
    }


    public Booking() {
    };
}