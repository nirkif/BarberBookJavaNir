package com.example.demo.Data;

import com.example.demo.Utility.GetTimeStamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Document("openings")
public class Opening {  //    every opening must have half an hour


    @Id
    private String id;
    private String barberUserName;
    private String barberName;
    public boolean isAvailable = true;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    public String openingInfo;
    public LocalDateTime timeStamp;

    public String getId() {
        return id;
    }

    public String getBarberUserName(){ return this.barberUserName; }

    public void setBarberUserName(String userName) { this.barberUserName = userName; }

    public String getBarberName(){ return this.barberName; }

    public void setBarberName(String name) { this.barberName = name; }

    public boolean getAvailability(){return this.isAvailable; }

    public void setAvailability(boolean status) { this.isAvailable = status; }

    public Opening(){}

    public Opening(String userName,String name,String startTime)
    {
        timeStamp = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.of(timeStamp.getYear(),
                timeStamp.getMonth(),
                timeStamp.getDayOfMonth(),
                Integer.parseInt(startTime)//  ---> hours
                ,0);// -----> min

        this.startTime = date;
        this.endTime = date.plusMinutes(30);
        this.openingInfo = " "+date.toString().split("T")[0] + "\n"+date.toString().split("T")[1]+" - "+endTime.toString().split("T")[1];

        this.barberUserName = userName;
        this.barberName = name;
        System.out.println(openingInfo);

    }
    public Opening(String userName, String name, String startTime,String startTimeMinutes)
    {
        timeStamp = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.of(timeStamp.getYear(),
                timeStamp.getMonth(),
                timeStamp.getDayOfMonth(),
                Integer.parseInt(startTime)// hours
                ,Integer.parseInt(startTimeMinutes));// min

        this.startTime = date;
        this.endTime = date.plusMinutes(30);
        this.openingInfo = " "+date.toString().split("T")[0]
                +"\n"+date.toString().split("T")[1]+" - "+endTime.toString().split("T")[1];

        this.barberUserName = userName;
        this.barberName = name;
        System.out.println(openingInfo);
    }
    public Opening(String userName, String name, String startTime,String startTimeMinutes,String dayOfMonth,String month)
    {
        timeStamp = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.of(timeStamp.getYear(),
                                              Integer.parseInt(month),
                                              Integer.parseInt(dayOfMonth),
                                              Integer.parseInt(startTime),
                                              Integer.parseInt(startTimeMinutes));
        this.startTime = date;
        this.endTime = date.plusMinutes(30);
        this.openingInfo = " "+date.toString().split("T")[0]
                +"\n"+date.toString().split("T")[1]+" - "+endTime.toString().split("T")[1];
        this.barberUserName = userName;
        this.barberName = name;
        System.out.println("created opening: "+openingInfo);
    }



    @Override
    public String toString() {
        return "new opening with "+barberName+" at "+startTime+" until "+endTime;

    }
}
