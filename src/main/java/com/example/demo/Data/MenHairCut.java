package com.example.demo.Data;

import java.time.LocalDateTime;

public class MenHairCut extends Booking{
    private int MenPrice = 50;
    public MenHairCut(String barberUsername, String username, String openingId, String openingInfo, LocalDateTime startTime,LocalDateTime endTime)
    {
        super.username = username;
        super.barberUsername = barberUsername;
        super.openingId = openingId;
        super.price = MenPrice;
        super.openingInfo = openingInfo;
        super.startTime = startTime;
        super.endTime = endTime;
    }

}
