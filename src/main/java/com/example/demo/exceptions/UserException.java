package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "video not found")
public class UserException extends RuntimeException{
    public UserException(String error){
        super("User Error: " + error);
    }
}
