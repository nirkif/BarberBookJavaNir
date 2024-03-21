package com.example.demo.Data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.RequestBody;

@Document("users")
public class Customer extends User{
    public Customer(@RequestBody User newCustomer){
        super(newCustomer.getUsername(), newCustomer.getName(), newCustomer.getPhoneNum());
        newCustomer.toString();
    }
    public Customer(){}

    @Override
    public String toString() {
        return "Type: Customer\nID: "+super.getId()+"\nUsername: "+super.getUsername()+"\nName: "+super.getName()+"\nPhone Number: "+super.getPhoneNum()+"\nCreated at: "+ created_at;
    }
}
