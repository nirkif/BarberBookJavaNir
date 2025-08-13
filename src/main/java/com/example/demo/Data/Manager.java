package com.example.demo.Data;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class Manager extends User{
    public Manager(User newManager)
    {
        super(newManager.getUsername(),newManager.getName(),newManager.getPhoneNum()); // שימוש בפונקציות של USER (אב) ליצירת אובייקט שונה
        super.setClassType(this.getClass().toString());
        System.out.println("promoted from user to manager");
    }
    public Manager(Barber newManager)
    {
        super(newManager.getUsername(),newManager.getName(),newManager.getPhoneNum()); // שימוש בפונקציות של USER (אב) ליצירת אובייקט שונה
        super.setClassType(this.getClass().toString());
        System.out.println("promoted from barber to manager");
    }
    public Manager(){}
}
