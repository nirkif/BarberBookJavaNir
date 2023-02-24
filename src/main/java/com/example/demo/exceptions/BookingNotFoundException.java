package com.example.demo.exceptions;

public class BookingNotFoundException extends  RuntimeException{
    public BookingNotFoundException(Long id){
        super("Could not find booking" + id);
    }
}
