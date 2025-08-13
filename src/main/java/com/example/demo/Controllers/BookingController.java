package com.example.demo.Controllers;


import com.example.demo.Data.*;
import com.example.demo.Repository.IBookingRepository;
import com.example.demo.Repository.IOpeningRepository;
import com.example.demo.Repository.IUserRepository;
import com.example.demo.Repository.IPricesRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RestController                         // נותן גישה להשתמש בבקשות REST כלומר GET,SET,DELETE
@CrossOrigin(origins = "*")             // מאשר בקשות HTTP לא משנה מאיפה הגיע הבקשה


public class BookingController {
    private final IBookingRepository bookingRepository;
    private final IOpeningRepository openingRepository;
    private final IUserRepository userRepository;
    private final IPricesRepository pricesRepository;
    private  int totalSum = 0;




    BookingController(@Autowired IOpeningRepository openingRepository,@Autowired IUserRepository userRepository, @Autowired IBookingRepository bookingRepository,@Autowired IPricesRepository pricesRepository) {
        this.openingRepository = openingRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.pricesRepository = pricesRepository;
    }
    //-------------------------------------------------------------------------------------------------------------------------------------//
    //                ||                         ||
    //                ||     GET REQUESTS        ||
    //                ||                         ||
    @CrossOrigin(origins = "*")
    @GetMapping("/getMyBooking/{username}")                                          // שימוש בפונקציה בעזרת URL
    List<Booking> getMyBooking(@PathVariable String username)                        // פונקציה לקבלת תור תפוס
    {
        List<Booking> bookingList = new ArrayList<>();                               // יצירת מערך של תורים תפוסים
        for (Booking booking:bookingRepository.findBookingsByusername(username))     // לולאת FOR בשביל לחפש תורים תפוסים לפי משתמש , מחפש תורים ב-DATABASE לפי משתנה username
        {
            if(booking.startTime.isAfter(LocalDateTime.now())){
                System.out.println("this booking is dated : "+booking.startTime);
                bookingList.add(booking);
            }
            else{
                System.out.println("this booking is NOT dated : "+booking.startTime+"!!");
                System.out.println("here are the following to count as profit: "+booking.openingInfo);
                //deleteBookingByID(booking.getId());
            }
        }
        System.out.println("fetched and deleted outdated bookings for "+username+".\n");
        return bookingList;                                                          // מחזיר את המערך של התורים התפוסים לפי אותו משתמש
    }
    //-------------------------------------------------------------------------------------------------------------------------------------//
    @GetMapping("/getBarberBooking/{barberUsername}")                               // שימוש בפונקציית בעזרת URL
    List<Booking> getBarberBooking(@PathVariable String barberUsername)             // פונקציה לקבלת כל התורים התפוסים שיש לספר
    {
        System.out.println("fetching "+barberUsername+" barber bookings.\n");
        return bookingRepository.findBookingsByBarberUsername(barberUsername);      // מחזיר מערך מה-DATABASE של כל התורים התפוסים של הספר
    }

    @GetMapping("/getAllBookings")
    List<Booking> getAllBookings()
    {
        List<Booking> allBookings = bookingRepository.findAll();
        System.out.println("fetching all bookings");
        return allBookings;
    }
    //-------------------------------------------------------------------------------------------------------------------------------------//
    //                ||                         ||
    //                ||    POST REQUESTS        ||
    //                ||                         ||


    @PostMapping("/setBookingV2")                                   //שימוש בפונקציית בעזרת URL
    String newOpeningV2(@RequestBody String body)                   //שינוי מתור פנוי לתור תפוס וסיווג תור אם הוא יותר מ1 אם כן בודק גם את התור הבא
    {
        JSONObject jsonObject = new JSONObject(body);               //יצירת אובייקט מסוג שמכיל את המשתנים של המחרוזת שהתקבלה
        System.out.println(body);
        Opening opening;                                            //יצירת משתמש מסוג תור פנוי
        Booking newBooking= null;
        Opening nextOpening = null;
        String haircutType = "";
        int price;
        try {
            List<Opening> availableOpenings = openingRepository.findAvailableOpeningsByBarberUsername(jsonObject.getString("barberUsername"),true);
            for(int i = 0 ; i < availableOpenings.size() ; i++)
                {
                if(availableOpenings.get(i).getId().equals(jsonObject.getString("openingId")) && i+1 <  availableOpenings.size()){
                    nextOpening = availableOpenings.get(i+1);
                }
            }
            opening = openingRepository.findOpeningByID(jsonObject.getString("openingId")); // חיפוש של תור פנוי לפי ID ע"י שליפת ID ממחרוזת שהתקבלה ויישום במשתמש OPENING
            haircutType = jsonObject.getString("haircutType");
            if(!opening.getAvailability())                                                      //בודק אם באובייקט המתשנה מסוג זמין הוא במצב FALSE
            {
                return "already booked "+opening.getId();                                       // אם כן מחזיר שהתור כבר תפוס
            }
            else {
                opening.setAvailability(false);                                                 // אחרת משנה את סוג המשתנה ל-FALSE
                openingRepository.save(opening);                                                // שומר את ה-OPENING שהתקבל
            }
        }

        catch (Exception e){                                                                    //אם משהו משתבש בתהליך מחזיר את ה-ERROR שהתקבל
            System.out.println(e.toString());
            return "invalid opening id!\n"+e.toString();
        }
        try {
            if (haircutType.equals("MenHairCut")){
                newBooking = new MenHairCut(jsonObject.getString("barberUsername"),
                        jsonObject.getString("username"),
                        jsonObject.getString("openingId"),
                        opening.openingInfo+"\nType: Men hair cut",opening.startTime,opening.endTime,pricesRepository.findPricesByID("688ba77d48d00c108a659d8a").getmenHairCutPrice());
            }
            else if(nextOpening == null){
                System.out.println("Appointment cannot be after closing hours!");
                opening.setAvailability(true);                                                 // אחרת משנה את סוג המשתנה ל-FALSE
                openingRepository.save(opening);
                return "Appointment cannot be after closing hours!";
            }
            else if (!opening.endTime.equals(nextOpening.startTime)){
                System.out.println("appointment cannot be : "+opening.startTime+" - "+nextOpening.endTime);
                opening.setAvailability(true);                                                 // אחרת משנה את סוג המשתנה ל-FALSE
                openingRepository.save(opening);
                return "appointment cannot be : "+opening.startTime+" - "+nextOpening.endTime;
            }
            else if (haircutType.equals("WomenHairDye")  && nextOpening.isAvailable){
                nextOpening.setAvailability(false);                                                 // אחרת משנה את סוג המשתנה ל-FALSE
                newBooking = new WomenHairDye(  jsonObject.getString("barberUsername"),
                        jsonObject.getString("username"),
                        jsonObject.getString("openingId"),
                        opening.openingInfo.substring(0,17)+nextOpening.openingInfo.substring(17)+"\nType: Women hair dye",
                        nextOpening.getId(),opening.startTime,opening.endTime.plusMinutes(30),pricesRepository.findPricesByID("688ba77d48d00c108a659d8a").getwomanHairDyePrice());
                openingRepository.save(nextOpening);                                                // שומר את ה-OPENING שהתקבל
            }
            else if (haircutType.equals("WomenHairCut")  && nextOpening.isAvailable){
                nextOpening.setAvailability(false);                                                 // אחרת משנה את סוג המשתנה ל-FALSE
                newBooking = new WomenHairCut(jsonObject.getString("barberUsername"),
                        jsonObject.getString("username"),
                        jsonObject.getString("openingId"),
                        opening.openingInfo.substring(0,17)+nextOpening.openingInfo.substring(17)+"\nType: Women hair cut",
                        nextOpening.getId(), opening.startTime,opening.endTime.plusMinutes(30),pricesRepository.findPricesByID("688ba77d48d00c108a659d8a").getwomanHairCutPrice());

                openingRepository.save(nextOpening);                                                // שומר את ה-OPENING שהתקבל
            }
            else {
                return "unsupported haircut type";
            }
            // יצירת משתמש מסוג תור תפוס מהסטרינג שהתקבל
            bookingRepository.save(newBooking);// שמירת התור התפוס בDATABASE
            return "new Booking created successfully for "+newBooking.getUsername()+" with "+newBooking.getBarberUsername();
        }
        catch (Exception err){                                                                                              // אם משהו משתבש
            try {                                                                                                           // הפיכת האובייקט במשתנה הזמינות ל-TRUE ושמירת המשתמש
                opening.setAvailability(true);
                openingRepository.save(opening);
                System.out.println("could not set booking and opening returned to previous form");
            }
            catch (Exception error){                                                                                        // אחרת מחזיר ERROR ומציג ID
                System.out.println(error.toString()+"\nFATAL error: booking not succeed and opening is not available "+opening.getId());
            }
            System.out.println(err.toString());
        }
        return "succefully booked "+newBooking.getId()+"on opening "+newBooking.getOpeningId();                             // מחזיר מחרוזת שכל התהליך התנהל בהתאם
    }
    //-------------------------------------------------------------------------------------------------------------------------------------//
    @PostMapping("/isDated")                     //שימוש בפונקציית בעזרת URL
    boolean isDated(@RequestBody String body) // בודק אם תור זמין בעזרת משתנה תאריך
    {
        JSONObject jsonObject = new JSONObject(body);// יצירת אובייקט עם המחרוזת שהתקבלה
        System.out.println("startTime: "+openingRepository.findOpeningByID(jsonObject.getString("openingId")));
        if(openingRepository.findOpeningByID(jsonObject.getString("openingId")).startTime.isBefore(LocalDateTime.now()))// חיפוש תור פנוי לפי ID ובנוסף בדיקה אם תור לא רלוונטי מבחינת תאריך
        {
            System.out.println("opening is outdated");
            System.out.println(openingRepository.findOpeningByID(jsonObject.getString("openingId")).startTime+" < "+LocalDateTime.now());
            return false;// אם לא רלוונטי מבחינת תאריך מחזיר FALSE
        }
        System.out.println("opening is dated");
        return true; // אם התאריך תקין מחזיר TRUE
    }
    //-------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/isOutDated")                     //שימוש בפונקציית בעזרת URL
    boolean isOutDated(@RequestBody String body) // בודק אם תור זמין בעזרת משתנה תאריך
    {
        JSONObject jsonObject = new JSONObject(body);// יצירת אובייקט עם המחרוזת שהתקבלה
        if(openingRepository.findOpeningByID(jsonObject.getString("openingId")).startTime.isBefore(LocalDateTime.now()))// חיפוש תור פנוי לפי ID ובנוסף בדיקה אם תור לא רלוונטי מבחינת תאריך
        {
            System.out.println("opening is outdated");
            System.out.println(openingRepository.findOpeningByID(jsonObject.getString("openingId")).startTime+" < "+LocalDateTime.now());
            return false;// אם לא רלוונטי מבחינת תאריך מחזיר FALSE
        }
        System.out.println("opening is dated");
        return true; // אם התאריך תקין מחזיר TRUE
    }
    //-------------------------------------------------------------------------------------------------------------------------------------


    boolean isDatedV2(String openingId)// בדיקה אם תור עדיין זמין בעזרת ID של תור פנוי
    {
        if(openingRepository.findOpeningByID(openingId).startTime.isBefore(LocalDateTime.now()))//מוצא את התור לפי ID ולאחר מכן בודק אם תחילת התור הוא לפני התאריך של היום
        {
            deleteBookingByID(openingId);
            return false;
        }
        return true;
    }


    //                ||                         ||
    //                ||   DELETE REQUESTS       ||
    //                ||                         ||


    @DeleteMapping("/deleteBooking")    // מחיקת תור קבוע והחזרה לתור פנוי
    String deleteBookingByID(@RequestBody String body)
    {
        System.out.println("body: "+body.toString());
        JSONObject bookingID = new JSONObject(body);
        Booking bookingToDelete = bookingRepository.findBookingByID(bookingID.getString("bookingId"));
        Opening opening = openingRepository.findOpeningByID(bookingToDelete.getOpeningId());
        System.out.println("opening from repository:"+bookingToDelete.getOpeningId());
        System.out.println(bookingToDelete.get2ndOpeningID() != null+" if status");
        if(bookingToDelete.get2ndOpeningID() != null){
            System.out.println("second opening id: "+bookingToDelete.get2ndOpeningID());
            Opening secondOpening = openingRepository.findOpeningByID(bookingToDelete.get2ndOpeningID());
            secondOpening.setAvailability(true);
            openingRepository.save(secondOpening);
            System.out.println("secondOpening status: "+secondOpening.getAvailability());
        }
        opening.setAvailability(true);
        try
        {
            bookingRepository.deleteById(bookingToDelete.getId());
            openingRepository.save(opening);
            System.out.println("Opening status: "+opening.getAvailability());
            System.out.println("booking deleted successfully.");
        }
        catch (Exception err)
        {
            System.out.printf(err.toString());
            return err.toString();
        }
        System.out.printf("Booking "+bookingToDelete.getId()+" has been deleted.");
        return "Booking "+bookingToDelete.getId()+" has been deleted.";
    }
//    @DeleteMapping("/deleteOutDatedBookings/")
//    String deleteOutDatedBookings()
//    {
//        System.out.println("starting deleteOutDatedBookings!");
//        List<Booking> allBookings = bookingRepository.findAll();
//        try{
//            for(Booking booking: allBookings)
//                if(booking.startTime.isBefore(LocalDateTime.now()))
//                    deleteBookingByID(booking.getId());
//            return "outdated booking deleted!";
//        }
//        catch (Exception err){
//            return err.toString();
//        }
//    }
    @GetMapping("/getProfit/")
    int getProfit(){
        List<Booking> allBookings = bookingRepository.findAll();
        totalSum=0;
        for(Booking booking: allBookings)
        {
            if(booking.startTime.isBefore(LocalDateTime.now()));
            {
                totalSum+=booking.getPrice();
            }
        }
        System.out.println("current total sum: "+totalSum);
        return totalSum;
    }

    @GetMapping("/getMonthlyProfit/")
    int getMonthlyProfit(){
        List<Booking> allBookings = bookingRepository.findAll();
        totalSum=0;
        for(Booking booking: allBookings)
        {
            LocalDateTime monthAgo= LocalDateTime.now().minusDays(30);
            if(booking.startTime.isAfter(monthAgo) && booking.startTime.isBefore(LocalDateTime.now()))
                totalSum+=booking.getPrice();

        }
        System.out.println("current monthly sum: "+totalSum);
        return totalSum;
    }

    @GetMapping("/getDailyProfit/")
    int getDailyProfit(){
        List<Booking> allBookings = bookingRepository.findAll();
        totalSum=0;
        for(Booking booking: allBookings)
        {
            LocalDateTime yesterDay = LocalDateTime.now().minusDays(1);
            if(booking.startTime.isAfter(yesterDay) && booking.startTime.isBefore(LocalDateTime.now()))
                totalSum+=booking.getPrice();
        }
        System.out.println("current Daily sum: "+totalSum);
        return totalSum;
    }

//    @GetMapping("/getOutdatedBookings")
//    Booking getOutDatedBookings(){
//        List<Booking> allBookings = bookingRepository.findAll();
//        List<Booking> allOutdatedBookings;
//        for(Booking booking: allBookings)
//        {
//            if(booking.startTime.isBefore(LocalDateTime.now()));
//            {
//
//            }
//        }
//    }


}