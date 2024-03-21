package com.example.demo.Controllers;

import com.example.demo.Data.Barber;
import com.example.demo.Data.Opening;
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

    //                ||   OPENING MAPPING    ||
    //                ||                      ||
    //                ||                      ||
    //                ||    POST REQUESTS     ||
    //                ||                      ||


    @PostMapping("/addOpening")
    void newOpening(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        Opening newOpening;
        System.out.println("adding opening");
        Barber barber = (Barber)userRepository.findUserByUserName(jsonObject.getString("userName"));
        try {
            newOpening = new Opening(barber.getUsername(),barber.getName(),jsonObject.getString("startTime"));
            openingRepository.save(newOpening);
            System.out.println("new opening created successfully with barber "+newOpening.getBarberName());
        }
        catch (Exception err){
            System.out.println(err.toString());
        }
    }
    @PostMapping("/addOpeningsV2")
    void newOpeningsV2(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        String username = jsonObject.getString("userName");
        String startTime =jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String month = jsonObject.getString("month");
        String dayOfMonth = jsonObject.getString("dayOfMonth");
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





    @PostMapping("/addOpenings")
    void newOpenings(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        String username = jsonObject.getString("userName");
        String startTime =jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        Barber barber = (Barber)userRepository.findUserByUserName(jsonObject.getString("userName"));
        System.out.println("creating day version 1.");
        try{
            int startTimeInt = Integer.parseInt(startTime); // by hours
            int endTimeInt = Integer.parseInt(endTime); // by hours 9 - 17
            if((startTimeInt < 23 && startTimeInt > -1 ) && (endTimeInt < 23 && endTimeInt > -1 && endTimeInt > startTimeInt))
            {
                int minutes = 0;
                while(startTimeInt < endTimeInt)
                {

                    Opening newOpening = new Opening(barber.getUsername(), barber.getName(), startTime ,String.valueOf(minutes));
                    openingRepository.save(newOpening);
                    minutes = (minutes+30)%60;
                    if(minutes == 0)
                        startTimeInt+=1;
                    startTime = String.valueOf(startTimeInt);
                }
            }
            System.out.println("day created successfully for "+barber.getUsername());


        }catch (Exception err) {
            System.out.println("error creating day: "+err);
        }
    }


    //                ||                      ||
    //                ||    GET REQUESTS      ||
    //                ||                      ||


    @GetMapping("/allOpenings/")
    List<Opening> allOpenings()
    {
        System.out.println("fetching all openings.");
        return openingRepository.findAll();
    }

    @GetMapping("/getOpenings/{username}")
    List<Opening> getOpeningsByUserName(@PathVariable String username)
    {
        System.out.printf("fetching openings for :"+username+".\n");
        return openingRepository.findOpeningsByBarberUsername(username);
    }
    @GetMapping("/getAvailableOpenings/{username}")
    List<Opening> getAvailableOpeningsByUserName(@PathVariable String username) // to add
    {
        System.out.println("fetching available openings for :"+username+".\n");
        return openingRepository.findAvailableOpeningsByBarberUsername(username,true);
    }
    @GetMapping("/findAllAvailableOpenings/")
    List<Opening> allAvailableOpenings()
    {
        System.out.println("fetching all available openings.\n");
        return openingRepository.findAllAvailableOpenings(true);
    }
    @DeleteMapping("/deleteOutDatedOpenings/")
    String deleteOutDatedOpenings()
    {
        List<Opening> openings = allAvailableOpenings();
        try{
            System.out.println("todays date: "+ LocalDateTime.now());
            for (Opening opening: openings)
            {
                System.out.println("opening date: "+opening.startTime);
                System.out.println("is statement true?"+opening.startTime.isBefore(LocalDateTime.now()));
                if(opening.startTime.isBefore(LocalDateTime.now()))
                {
                    System.out.println("removing opening that scheduled to"+opening.startTime);
                    openingRepository.deleteById(opening.getId());
                }
            }
            System.out.println("removed outdated openings.");
            return "openings removed!";
        }catch (Exception err) {
            System.out.println("Error: "+err);
            return err.toString();
        }


    }


    //               ||                       ||
    //               ||   DELETE REQUESTS     ||
    //               ||                       ||


    @DeleteMapping("/deleteOpening")

    String deleteUserByID(@RequestBody String body)
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
