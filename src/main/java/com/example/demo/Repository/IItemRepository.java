package com.example.demo.Repository;
import com.example.demo.Data.Item;
import com.example.demo.Data.Opening;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

 public interface IItemRepository extends MongoRepository<Item,String>
 {
        @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
        Opening findAll(String category);

        @Query("{_id:'?0'}")
        Item findItemByID(String id);

}
