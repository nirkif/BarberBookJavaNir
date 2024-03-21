package com.example.demo.Controllers;


import com.example.demo.Data.Barber;
import com.example.demo.Repository.IBookingRepository;
import com.example.demo.Repository.IOpeningRepository;
import com.example.demo.Repository.IUserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.exceptions.BookingNotFoundException;
import com.example.demo.exceptions.OpeningNotFoundException;


import com.example.demo.Data.Booking;
import com.example.demo.Data.Opening;

import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@RestController
@CrossOrigin(origins = "*")
public class BookingController {
    private final IBookingRepository bookingRepository;
    private final IOpeningRepository openingRepository;
    private final IUserRepository userRepository;
    private final int bookingPrice = 60;
    BookingController(@Autowired IOpeningRepository openingRepository,@Autowired IUserRepository userRepository, @Autowired IBookingRepository bookingRepository) {
        this.openingRepository = openingRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }
    @CrossOrigin(origins = "*")


    //                ||   BOOKING MAPPING    ||
    //                ||                      ||
    //                ||                      ||
    //                ||    POST REQUESTS     ||
    //                ||                      ||


    @PostMapping("/setBookingV2")
    String newOpeningV2(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        System.out.println(body);
        Opening opening;
        Booking newBooking= null;
        try {
            opening = openingRepository.findOpeningByID(jsonObject.getString("openingId"));
            if(opening.getAvailability() == false)
            {
                return "already booked "+opening.getId();
            }
            else {
                opening.setAvailability(false);
                openingRepository.save(opening);
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return "invalid opening id";
        }
        try {

            newBooking = new Booking(jsonObject.getString("barberUsername"), jsonObject.getString("username"), jsonObject.getString("openingId"),bookingPrice,opening.openingInfo);
            bookingRepository.save(newBooking);
            return "new Booking created successfully for "+newBooking.getUsername()+" with "+newBooking.getBarberUsername();
        }
        catch (Exception err){
            try {
                opening.setAvailability(true);
                openingRepository.save(opening);
                System.out.println("could not set booking and opening returned to previous form");
            }
            catch (Exception error){
                System.out.println(error.toString()+"\nFATAL error: booking not succeed and opening is not available "+opening.getId());
            }
            System.out.println(err.toString());
        }
        return "succefully booked "+newBooking.getId()+"on opening "+newBooking.getOpeningId();

    }
    @PostMapping("/isDated")
    boolean isDated(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        System.out.println("startTime: "+openingRepository.findOpeningByID(jsonObject.getString("openingId")));
        if(openingRepository.findOpeningByID(jsonObject.getString("openingId")).startTime.isBefore(LocalDateTime.now()))
        {
            System.out.println("opening is outdated");
            System.out.println(openingRepository.findOpeningByID(jsonObject.getString("openingId")).startTime+" < "+LocalDateTime.now());
            return false;
        }
        System.out.println("opening is dated");
        return true; // only if true will show the booking
    }


    //                ||                      ||
    //                ||    GET REQUESTS      ||
    //                ||                      ||


    @GetMapping("/getMyBooking/{username}")
    List<Booking> getMyBooking(@PathVariable String username)
    {
        List<Booking> bookingList = new ArrayList<>();
        for (Booking booking:bookingRepository.findBookingsByusername(username))
        {
            if(isDatedV2(booking.getOpeningId()))
                bookingList.add(booking);
        }
        System.out.println("fetched bookings for "+username+".\n");
        return bookingList;
    }
    @GetMapping("/getBarberBooking/{barberUsername}")
    List<Booking> getBarberBooking(@PathVariable String barberUsername)
    {
        System.out.println("fetching "+barberUsername+" barber bookings.\n");
        return bookingRepository.findBookingsByBarberUsername(barberUsername);
    }
    //                ||                      ||
    //                ||   DELETE REQUESTS    ||
    //                ||                      ||


    @DeleteMapping("/deleteBooking")

    String deleteUserByID(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        String bookingId = jsonObject.getString("bookingId");
        String openingId = bookingRepository.findBookingByID(bookingId).getOpeningId();
        Opening opening = openingRepository.findOpeningByID(openingId);
        opening.setAvailability(true);
        openingRepository.save(opening);
        System.out.println("opening availabily returned to true.");
        try
        {
            bookingRepository.deleteById(bookingId);
            System.out.println("booking deleted successfully.");
        }
        catch (Exception err)
        {
            opening.setAvailability(false);
            openingRepository.save(opening);
            System.out.printf(err.toString());
            return err.toString();
        }
        System.out.printf("Booking "+bookingId+" has been deleted.");
        return "Booking "+bookingId+" has been deleted.";
    }

    //                ||                      ||
    //                ||      AUXILIARY       ||
    //                ||                      ||
    boolean isDatedV2(String openingId)
    {
        if(openingRepository.findOpeningByID(openingId).startTime.isBefore(LocalDateTime.now()))
        {
            return false;
        }
        return true;
    }





}
