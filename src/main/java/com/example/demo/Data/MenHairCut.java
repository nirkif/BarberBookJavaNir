package com.example.demo.Data;
import java.time.LocalDateTime;

public class MenHairCut extends Booking{
    public MenHairCut(String barberUsername, String username, String openingId, String openingInfo, LocalDateTime startTime,LocalDateTime endTime,int price)
    {
        super.username = username;
        super.barberUsername = barberUsername;
        super.openingId = openingId;
        super.openingInfo = openingInfo;
        super.startTime = startTime;
        super.endTime = endTime;
        super.price = price;
    }
}
