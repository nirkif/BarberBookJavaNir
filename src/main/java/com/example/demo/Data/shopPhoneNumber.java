package com.example.demo.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shopPhoneNumber")
public class shopPhoneNumber {
    @Id
    private String id;
    String phoneNumber;
    public shopPhoneNumber(String phoneNumber)
    {
        this.phoneNumber=phoneNumber;
    }
    public String getPhoneNumber(){return this.phoneNumber;}
    public void setPhoneNumber(String newPhoneNumber){this.phoneNumber=newPhoneNumber;}
    public String toString(){
        return "Contact the Shop: "+this.phoneNumber;
    }

}
