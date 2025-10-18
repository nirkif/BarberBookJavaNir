package com.example.demo.Repository;
import com.example.demo.Data.Opening;
import com.example.demo.Data.Prices;
import com.example.demo.Data.shopPhoneNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
public interface IShopPhoneNumberRepository extends MongoRepository<shopPhoneNumber,String>{
    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    Opening findAll(String category);

    @Query("{_id:'?0'}")
    shopPhoneNumber findShopPhoneNumberById(String id);
}
