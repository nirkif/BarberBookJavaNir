package com.example.demo.Controllers;

import com.example.demo.Data.Barber;
import com.example.demo.Data.Password;
import com.example.demo.Data.encryption;
import com.example.demo.Repository.IPasswordRepository;
import com.example.demo.exceptions.UserException;
import com.example.demo.Data.User;
import com.example.demo.Repository.IUserRepository;

import org.json.JSONObject;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController{
    private final IUserRepository repository;
    private final IPasswordRepository passRepository;
    UserController(@Autowired IUserRepository repository, @Autowired IPasswordRepository passRepository)
    {
        this.repository = repository;
        this.passRepository = passRepository;
    }
    private com.example.demo.Data.encryption encryption = new encryption();



    //                ||    USER MAPPING      ||
    //                ||                      ||
    //                ||                      ||
    //                ||    POST REQUESTS     ||
    //                ||                      ||

    @PostMapping("/addUser")                        // הוספת משתמש
    User newUser(@RequestBody String body)
    {
        JSONObject jsonObject = new JSONObject(body);
        try
        {
            int offSet = LocalDateTime.now().getSecond();
            User newUser = new User(jsonObject.getString("username"),jsonObject.getString("name"),jsonObject.getString("phoneNumber"));
            User user= repository.findUserByUserName(newUser.getUsername());
            if(user != null){
                throw new UserException("addUser function error, Username already exists");
            }
            String p = encryption.caesarEncrypt(jsonObject.getString("password"),offSet);
            Password pass = new Password(jsonObject.getString("username"),p,offSet);
            System.out.println(newUser.toString());
            passRepository.save(pass);
            System.out.println("new user created: ");
            return repository.save(newUser);

        }
        catch (Exception err)
        {
            System.out.println(err.toString());
            throw new UserException("addUser function error, something in the process addUser did not succeed");
        }
    }

    @PostMapping("/checkPassword")              // בדיקת סיסמה אם קלט
    boolean checkPassword(@RequestBody String body)
    {
        JSONObject object = new JSONObject(body);
        Password p = passRepository.findPassByUsername(object.getString("username")).get(0);
        //System.out.println("password found from "+object.getString("username")+" = "+ encryption.caesarEncrypt(p.getEncryptedPassword(),-p.getOffSet()));
        String toCompare = encryption.caesarEncrypt(object.getString("password"),p.getOffSet());
        if(p.getEncryptedPassword().equals(toCompare))
        {
            System.out.println("returning TRUE  for password field");
            return true;
        }
        System.out.println("returning FALSE  for password field");
        return false;
    }

    @PostMapping("/changeProfilePicture")       // שינוי תמונת פרופיל
    void changeProfilePicture(@RequestBody String body)
    {
        JSONObject object = new JSONObject(body);
        User user = repository.findUserByUserName(object.getString("username"));
        user.setImageuri(object.getString("imageUri"));
        repository.save(user);
        System.out.println("user "+user.getUsername()+" profile picture updated.");
    }

    //                ||                      ||
    //                ||    GET REQUESTS      ||
    //                ||                      ||

    @GetMapping("/allUsers")            // כל המשתמשים
    List<User> all()
    {
        System.out.println("fetching all users.");
        return repository.findAll();
    }
    @GetMapping("/onlyUsers")           // רק משתמשים
    List<User> onlyUsers()
    {
        List<User> users = new ArrayList<>();
        System.out.println("fetching only users.");
        try
        {
            for(User user : all())
            {
                if(!user.getClass().equals(Barber.class))
                    users.add(user);
            }
        }
        catch (Exception err)
        {
            System.out.println(err.toString());
            return null;
        }
        System.out.println("only users fetched successfully");
        return users;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/findByUserName/{userName}")
    User getUserByUserName(@PathVariable String userName)           // קבלת משתמש לפי שם משתמש
    {
        System.out.println("username to search: "+userName);
        try
        {
            User user= repository.findUserByUserName(userName);
            if(user != null){
                System.out.println(user.getUsername()+" fetched successfully.");
                return user;
            }
            else{
                System.out.println("findByUserName function error, User not found");
                return null;
            }
        }
        catch (Exception err) {
            System.out.println(err.toString());
            throw new UserException("findByUserName function error, something went wrong in the process");
        }
    }
    @GetMapping("/findByID/{id}")                   // קבלת משתמש לפי ID
    User getUserByID(@PathVariable String id)
    {
        try
        {
            User result = repository.findUserById(id);
            System.out.println("user found with id.");
            System.out.println(result.toString());
            return result;
        }
        catch (Exception err)
        {
            System.out.println(err.toString());
            return null;
        }
    }

    //                ||                      ||
    //                ||   DELETE REQUESTS    ||
    //                ||                      ||

    @DeleteMapping("/deleteUser")           // מחיקת משתמש

    Void deleteUserByID(@RequestBody String body)
    {
        String id;
        try {
            JSONObject jsonObject = new JSONObject(body);
            id = jsonObject.getString("id");
        }
        catch(Exception err)
        {
            id = body;
        }

        try
        {
            repository.deleteById(id);
        }
        catch (Exception err)
        {
            System.out.printf(err.toString());
        }
        System.out.printf("User "+id+" has been deleted.");
        return null;
    }

    //                ||    BARBER MAPPING    ||
    //                ||                      ||
    //                ||                      ||
    //                ||    POST REQUESTS     ||
    //                ||                      ||
    @PostMapping("/addBarberFromUserId")                    // הוספת ספר מID של משתמש
    Barber userToBarber(@RequestBody String body)
    {
        JSONObject object = new JSONObject(body);
        String id = object.getString("id");
        User u = repository.findUserById(id);
        System.out.println("converting user to barber by id.");
        try{
            if(!repository.findUserById(id).getClass().equals(com.example.demo.Data.Barber.class))
            {
                Barber newBarber = new Barber(u);
                System.out.println("new barber created :"+newBarber.toString());
                deleteUserByID(id);
                System.out.println("user been removed and converted to barber");
                return repository.save(newBarber);
            }
            else{
                System.out.println("cannot create new barber from a barber");
                System.out.println(repository.findUserById(object.getString("id")).getClass().equals(com.example.demo.Data.User.class));
            }
        }
        catch (Exception error)
        {
            System.out.println(error);
        }
        return null;
    }


    //                ||                      ||
    //                ||    GET  REQUESTS     ||
    //                ||                      ||

    @GetMapping("/allBarbers")          // כל הספרים
    List<User> allBarbers()
    {
        List<User> barbers = new ArrayList<>();
        System.out.println("fetching barbers.");
        try
        {
            for(User user : all())
            {
                if(user.getClass().equals(Barber.class))
                    barbers.add(user);
            }
        }
        catch (Exception err)
        {
            System.out.println(err.toString());
            return null;
        }
        System.out.println("barbers fetched successfully");
        return barbers;
    }

}