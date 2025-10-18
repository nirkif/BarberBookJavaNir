package com.example.demo.Controllers;

import com.example.demo.Data.shopPhoneNumber;
import com.example.demo.Repository.IShopPhoneNumberRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ShopPhoneNumberController {
    private final IShopPhoneNumberRepository shopPhoneNumberRepository;
    ShopPhoneNumberController(@Autowired IShopPhoneNumberRepository shopPhoneNumberRepository){this.shopPhoneNumberRepository = shopPhoneNumberRepository;}
    @PostMapping("/createNewShopPhoneNumber")
    public void createNewShopPhoneNumber(){
        try{
            shopPhoneNumber newShopPhoneNumber = new shopPhoneNumber("000-000000");
            System.out.println(newShopPhoneNumber.toString());
            shopPhoneNumberRepository.save(newShopPhoneNumber);
        }
        catch (Exception err){System.out.println(err.toString());}
    }
    @GetMapping("/getAllShopPhoneNumbers")
    public List<shopPhoneNumber> getAllShopPhoneNumber(){return shopPhoneNumberRepository.findAll();}
    @GetMapping("/getMainShopPhoneNumber")
    public shopPhoneNumber getShopPhoneNumber(){return shopPhoneNumberRepository.findShopPhoneNumberById("68f3a41f0f8b126792a70f1e");}
    @PutMapping("/updateShopPhoneNumber")
    public void updateShopPhoneNumber(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            shopPhoneNumber mainShopPhoneNumber = shopPhoneNumberRepository.findShopPhoneNumberById("68f3a41f0f8b126792a70f1e");
            String newShopPhoneNumber = jsonObject.getString("phoneNumber");
            mainShopPhoneNumber.setPhoneNumber(newShopPhoneNumber);
            shopPhoneNumberRepository.save(mainShopPhoneNumber);
            System.out.println("updated shop phone number to: " + mainShopPhoneNumber);
        }
        catch (Exception err){
            System.out.println(err.toString());
        }
    }


}
