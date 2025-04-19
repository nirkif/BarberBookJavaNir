package com.example.demo.Data;

import java.time.LocalDateTime;

public class WomenHairDye extends Booking{
    private int HairDyePrice = 200;
    public WomenHairDye(String barberUsername, String username, String openingId, String openingInfo, String opening2Id, LocalDateTime startTime,LocalDateTime endTime)
    {
        super.username = username;
        super.barberUsername = barberUsername;
        super.openingId = openingId;
        super.price = HairDyePrice;
        super.openingInfo = openingInfo;
        super.opening2Id = opening2Id;
        super.startTime = startTime;
        super.endTime = endTime;
    }
}
