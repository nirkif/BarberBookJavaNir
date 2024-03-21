package com.example.demo.Repository;

import com.example.demo.Data.Booking;
import com.example.demo.Data.Opening;
import com.example.demo.Data.Password;
import com.example.demo.Data.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IPasswordRepository extends MongoRepository<Password,String > {
    @Query("{ 'username' : '?0' }")
    Password findByuserName(String username);

    @Query("{username:'?0'}")
    List<Password> findPassByUsername(String username);
}
