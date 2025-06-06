package com.example.demo.Data;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("passwords")
public class Password {
    @Indexed(unique = true)
    private String username;
    private String encryptedPassword;

    private int offSet = LocalDateTime.now().getSecond();

    public Password(String username, String encryptedPassword,int offSet)
    {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.offSet = offSet;
    }
    public String getUsername() {return this.username;}
    public String getEncryptedPassword() {return this.encryptedPassword;}
    public int getOffSet(){return this.offSet;}


}
