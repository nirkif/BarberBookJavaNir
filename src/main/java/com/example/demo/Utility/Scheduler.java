package com.example.demo.Utility;

import com.example.demo.Repository.IBookingRepository;
import com.example.demo.Repository.IOpeningRepository;
import com.example.demo.Repository.IUserRepository;
import com.example.demo.Data.Booking;
import com.example.demo.Data.Opening;
import com.example.demo.Data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class Scheduler {
    private final IBookingRepository bookingRepository;
    private final IOpeningRepository openingRepository;
    private final IUserRepository userRepository;
    
    LocalDateTime timestamp = LocalDateTime.now();
    Scheduler(@Autowired IOpeningRepository openingRepository, @Autowired IUserRepository userRepository, @Autowired IBookingRepository bookingRepository) {
        this.openingRepository = openingRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }
    @Scheduled(cron = "0 0 8 * * ?")
    public void notifyAllUsers()
    {
        for (Booking book:bookingRepository.findAll())
        {
            if(openingRepository.findOpeningByID(book.getOpeningId()).startTime.getDayOfMonth() == LocalDateTime.now().getDayOfMonth() &&
                    openingRepository.findOpeningByID(book.getOpeningId()).startTime.getMonth() == LocalDateTime.now().getMonth())
            {
                User user = userRepository.findUserByUserName(book.getUsername());
                System.out.println(user.getUsername()+" has an appointment at "+book.openingInfo);
            }
        }
    }
    
}
