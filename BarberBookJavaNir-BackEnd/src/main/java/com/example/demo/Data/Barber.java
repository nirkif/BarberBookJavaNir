package com.example.demo.Data;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document("users")
public class Barber extends User{
    public Barber(User newBarber)
    {
        super(newBarber.getUsername(),newBarber.getName(),newBarber.getPhoneNum());
        super.setClassType(this.getClass().toString());
    }
    public Barber(){}

    @Override
    public String toString() {
        return super.toString();
    }
}
