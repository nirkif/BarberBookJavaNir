package com.example.demo.Controllers;

import com.example.demo.Data.Prices;
import com.example.demo.Repository.IPricesRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

@RestController
@CrossOrigin
public class PriceController {
    private final IPricesRepository pricesRepository;
    PriceController(@Autowired IPricesRepository pricesRepository){this.pricesRepository = pricesRepository;}

    @PutMapping("/changeWomanHairCutPrice")
    public void changeWomanHairCutPrice(@RequestBody String body)
    {
        try{
            JSONObject updatedPrices = new JSONObject(body);
            Prices mainPriceObject = pricesRepository.findPricesByID("688ba77d48d00c108a659d8a");
            String oldPricesToString = mainPriceObject.toString();
            System.out.println("Old Prices: "+oldPricesToString);
            mainPriceObject.setwomanHairCutPrice(updatedPrices.getInt("womanHairCutPrice"));
            mainPriceObject.setLastUpdated(LocalDateTime.now());
            if(oldPricesToString.equals(mainPriceObject.toString()))
                changeWomanHairCutPrice(mainPriceObject.toString());
            pricesRepository.save(mainPriceObject);
            System.out.println("Updated Prices: "+mainPriceObject.toString());
        }
        catch (Exception err){System.out.println(err.toString());}
    }
    @PutMapping("/changeWomanHairDyePrice")
    public void changeWomanHairDyePrice(@RequestBody String body)
    {
        try{
            JSONObject updatedPrices = new JSONObject(body);
            Prices mainPriceObject = pricesRepository.findPricesByID("688ba77d48d00c108a659d8a");
            mainPriceObject.setWomanHairDyePrice(updatedPrices.getInt("womanHairDyePrice"));
            mainPriceObject.setLastUpdated(LocalDateTime.now());
            pricesRepository.save(mainPriceObject);
        }
        catch (Exception err){System.out.println(err.toString());}
    }
    @PutMapping("/changeMenHairCutPrice")
    public void changeMenHairCutPrice(@RequestBody String body)
    {
        try{
            JSONObject updatedPrices = new JSONObject(body);
            Prices mainPriceObject =  pricesRepository.findPricesByID("688ba77d48d00c108a659d8a");
            System.out.println("Old Prices: "+mainPriceObject.toString());
            mainPriceObject.setmenHairCutPrice(updatedPrices.getInt("menHairCutPrice"));
            mainPriceObject.setLastUpdated(LocalDateTime.now());
            pricesRepository.save(mainPriceObject);
            System.out.println("Updated Prices: "+mainPriceObject.toString());
        }
        catch (Exception err){System.out.println(err.toString());}
    }

    @GetMapping("/allPriceObject")
    List<Prices> allPrices(){
        List<Prices> allPrices = pricesRepository.findAll();
        return allPrices;
    }
    @PostMapping("/createPricesObject")
    public void createPricesObject(){
        try{
            Prices PriceObject = new Prices(50,100,150);
            System.out.println(PriceObject.toString());
            pricesRepository.save(PriceObject);
        }
        catch (Exception err){System.out.println(err.toString());}
    }
    @GetMapping("/pricesToString")
    public String pricesToString()
    {
        System.out.println(pricesRepository.findPricesByID("688ba77d48d00c108a659d8a").toString());
        return pricesRepository.findPricesByID("688ba77d48d00c108a659d8a").toString();
    }



}
