package com.example.demo.Data;

//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Document("users")
public class User {
    @Id
//    @GeneratedValue
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String phoneNumber;

    private String name;
    private String classType = this.getClass().getSimpleName();

    public String getClassType(){ return classType; }
    public void setClassType(String classString) { this.classType = classString; }
    public LocalDate created_at = LocalDate.now();

    public String imageuri = "https://www.nicepng.com/png/detail/933-9332131_profile-picture-default-png.png";


    public User() {
    }

    ;

    public String getPhoneNum() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImageuri(){return this.imageuri;}

    public void setImageuri(String newUri){this.imageuri = newUri;}

    public User(String username, String name, String phoneNumber) {
        this.username = username;
        this.name = name;
        created_at = LocalDate.now();
        this.phoneNumber = phoneNumber;
        this.toString();
    }
    @Override
    public String toString() {
        return "userName:"+getUsername()+"\n" +
                "name: "+getName()+"\n" +
                "phoneNumber: "+getPhoneNum()+"\n" +
                "created at: "+created_at+"\n" +
                "type: "+classType;
    }

}