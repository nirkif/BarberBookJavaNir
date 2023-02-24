package com.example.demo.Repository;

import com.example.demo.Data.Booking;
import com.example.demo.Data.Opening;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.awt.print.Book;
import java.util.List;
public interface IBookingRepository extends MongoRepository<Booking,String> {
    @Query("{id:'?0'}")
    Booking findBookingByID(String id);


    @Query("{barberUsername:'?0'}")
    List<Booking> findBookingsByBarberUsername(String barberUsername);

    @Query("{username:'?0'}")
    List<Booking> findBookingsByusername(String username);

}
