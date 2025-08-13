package com.example.demo.Controllers;

import com.example.demo.Data.Item;
import com.example.demo.Repository.IItemRepository;
import com.twilio.twiml.messaging.Body;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ItemController {
    private final IItemRepository itemRepository;
    ItemController(@Autowired IItemRepository itemRepository) {this.itemRepository = itemRepository;}

        @PostMapping("/newItem")
        String newItem(@RequestBody String body)
        {
            try{
                JSONObject jsonObject = new JSONObject(body);
                Item item = new Item(jsonObject.getString("name"),
                        jsonObject.getString("supplier"),
                        jsonObject.getInt("price"),
                        jsonObject.getInt("quantity"));
                itemRepository.save(item);
                return item.toString();
            }
            catch (Exception err){
                return err.toString();
            }

        }
        @GetMapping("getItemByID/{id}")
        Item getItemByID(@PathVariable String id)
        {
            try {
                Item item = itemRepository.findItemByID(id);
                System.out.println(item.toString());
                return item;
            }
            catch(Exception err){
                System.out.println(err.toString());
                return new Item("","",0,0);
            }
        }
        @PutMapping("updateItemQuantityByID")
        List<Item> updateItemQuantity(@RequestBody String body)
        {
            try{
                JSONObject jsonObject = new JSONObject(body);
                Item item = itemRepository.findItemByID(jsonObject.getString("id"));
                item.setQuantity(jsonObject.getInt("quantity"));
                itemRepository.save(item);
                System.out.println("item updated succesfully: "+item.getName());
                return itemRepository.findAll();
            }
            catch (Exception err){return itemRepository.findAll();}
        }
        @DeleteMapping("DeleteItemByID/{id}")
        List<Item> DeleteItemByID(@PathVariable String id)
        {
            try{
                itemRepository.deleteById(id);
                System.out.println("item deleted successfully");
            }
            catch (Exception err){System.out.println(err.toString());}
            return itemRepository.findAll();
        }
        @GetMapping("getAllItems")
        List<Item> getAllItems()
        {
            try {
                List<Item> items = itemRepository.findAll();
                for (Item item : items)
                    if (item.getQuantity() == 0){
                        itemRepository.deleteById(item.getId());
                        itemRepository.findAll();
                    }
                return items;
            }catch(Exception err){
                System.out.println(err.toString());
                return null;
        }}
}
