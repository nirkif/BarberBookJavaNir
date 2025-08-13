package com.example.demo.Data;

import com.example.demo.Repository.IPricesRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

public class WomenHairCut extends Booking {
    public WomenHairCut(String barberUsername, String username, String openingId, String openingInfo, String opening2Id, LocalDateTime startTime,LocalDateTime endTime,int price)
    {
        super.username = username;
        super.barberUsername = barberUsername;
        super.openingId = openingId;
        super.openingInfo = openingInfo;
        super.opening2Id = opening2Id;
        super.startTime = startTime;
        super.endTime = endTime;
        super.price = price;
    }
}

