package com.example.demo.Repository;
import com.example.demo.Data.Opening;
import com.example.demo.Data.Prices;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface IPricesRepository extends MongoRepository<Prices,String> {
    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    Opening findAll(String category);

    @Query("{_id:'?0'}")
    Prices findPricesByID(String id);

}
