package com.example.demo.Controllers;

import com.example.demo.Data.Barber;
import com.example.demo.Data.Manager;
import com.example.demo.Data.Opening;
import com.example.demo.Data.User;
import com.example.demo.Repository.IOpeningRepository;
import com.example.demo.Repository.IUserRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class OpeningController{
    private final IOpeningRepository openingRepository;
    private final IUserRepository userRepository;
    OpeningController(@Autowired IOpeningRepository iOpeningRepository, @Autowired IUserRepository userRepository) {
        this.openingRepository = iOpeningRepository;
        this.userRepository = userRepository;
    }
    //                ||                         ||
    //                ||     POST REQUESTS       ||
    //                ||                         ||

    @PostMapping("/addOpeningsV2") // יצירת אובייקט מסוג תור פנוי
    void newOpeningsV2(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        String username = jsonObject.getString("userName");
        String startTime =jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String month = jsonObject.getString("month");
        String dayOfMonth = jsonObject.getString("dayOfMonth");
        System.out.println(userRepository.findUserByUserName(jsonObject.getString("userName")));
        System.out.println(userRepository.findUserByUserName(jsonObject.getString("userName")).getClassType());
        User user = userRepository.findUserByUserName(jsonObject.getString("userName"));
        if(user != null && user.getClass().equals(Barber.class))
        {
            Barber barber = (Barber)userRepository.findUserByUserName(jsonObject.getString("userName"));
            System.out.println("creating day version 2.");
            try{
                int startTimeInt = Integer.parseInt(startTime); // by hours
                int endTimeInt = Integer.parseInt(endTime); // by hours 9 - 17
                if((startTimeInt < 23 && startTimeInt > -1 ) && (endTimeInt < 23 && endTimeInt > -1 && endTimeInt > startTimeInt))
                {
                    int minutes = 0;
                    while(startTimeInt < endTimeInt)
                    {

                        Opening newOpening = new Opening(barber.getUsername(), barber.getName(), startTime ,String.valueOf(minutes),dayOfMonth,month);
                        openingRepository.save(newOpening);
                        minutes = (minutes+30)%60;
                        if(minutes == 0)
                            startTimeInt+=1;
                        startTime = String.valueOf(startTimeInt);
                    }
                }
                System.out.println("day created successfully for "+barber.getUsername()+" from 9 - 17");


            }catch (Exception err) {
                System.out.println("error creating day: "+err);
            }

        }
        else if (user != null && user.getClass().equals(Manager.class))
        {
            Manager manager = (Manager)userRepository.findUserByUserName(jsonObject.getString("userName"));
            try{
                int startTimeInt = Integer.parseInt(startTime); // by hours
                int endTimeInt = Integer.parseInt(endTime); // by hours 9 - 17
                if((startTimeInt < 23 && startTimeInt > -1 ) && (endTimeInt < 23 && endTimeInt > -1 && endTimeInt > startTimeInt))
                {
                    int minutes = 0;
                    while(startTimeInt < endTimeInt)
                    {

                        Opening newOpening = new Opening(manager.getUsername(), manager.getName(), startTime ,String.valueOf(minutes),dayOfMonth,month);
                        openingRepository.save(newOpening);
                        minutes = (minutes+30)%60;
                        if(minutes == 0)
                            startTimeInt+=1;
                        startTime = String.valueOf(startTimeInt);
                    }
                }
        } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }
    }
    //________________________________________________________________________________________________________________//




    //                ||                         ||
    //                ||     GET REQUESTS        ||
    //                ||                         ||
    @GetMapping("/allOpenings/") // קבלת כל התורים
    List<Opening> allOpenings()
    {
        System.out.println("fetching all openings.");
        return openingRepository.findAll();
    }
    //________________________________________________________________________________________________________________//
    @GetMapping("/getOpenings/{username}")              // קבלת תור לפי שם
    List<Opening> getOpeningsByUserName(@PathVariable String username)
    {
        System.out.printf("fetching openings for :"+username+".\n");
        return openingRepository.findOpeningsByBarberUsername(username);
    }
    //________________________________________________________________________________________________________________//
    @GetMapping("/getAvailableOpenings/{username}")         // קבלת זמינות תור
    List<Opening> getAvailableOpeningsByUserName(@PathVariable String username) // to add
    {
        System.out.println("fetching available openings for :"+username+".\n");
        return openingRepository.findAvailableOpeningsByBarberUsername(username,true);
    }
    //________________________________________________________________________________________________________________//
    @GetMapping("/findAllAvailableOpenings/")       // קבלת כל התורים הזמינים
    List<Opening> allAvailableOpenings()
    {
        System.out.println("fetching all available openings.\n");
        return openingRepository.findAllAvailableOpenings(true);
    }


    //                ||                         ||
    //                ||   DELETE REQUESTS       ||
    //                ||                         ||
    @DeleteMapping("/deleteOutDatedOpenings/")      // מחיקת תורים לא זמינים
    String deleteOutDatedOpenings()
    {
        List<Opening> openings = openingRepository.findAll();
        try{
            System.out.println("todays date: "+ LocalDateTime.now());
            List<Opening> outDatedOpenings = new ArrayList<>();
            for (Opening opening: openings)
            {
                if(opening.startTime.isBefore(LocalDateTime.now()))
                {
                    System.out.println("removing opening that scheduled to"+opening.startTime);
                    outDatedOpenings.add(opening);
                }
            }
            openingRepository.deleteAll(outDatedOpenings);
            System.out.println("removed outdated openings.");
            return "removed outdated openings!";
        }catch (Exception err) {
            System.out.println("Error: "+err);
            return err.toString();
        }


    }
    //________________________________________________________________________________________________________________//
    @DeleteMapping("/deleteOpening")        // מחיקת תור
    String deleteOpeningByID(@RequestBody String body)
    {

        JSONObject jsonObject = new JSONObject(body);
        String id = jsonObject.getString("openingId");
        try
        {
            if(!openingRepository.findOpeningByID(id).getAvailability())
                return "cannot delete because opening already booked.";
            openingRepository.deleteById(id);
        }
        catch (Exception err)
        {
            System.out.printf(err.toString());
        }
        System.out.printf("Opening "+id+" has been deleted.");
        return "Opening "+id+" has been deleted.";
    }
}