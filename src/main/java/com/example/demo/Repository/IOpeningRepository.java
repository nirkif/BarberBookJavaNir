package com.example.demo.Repository;
import com.example.demo.Data.Opening;
import com.example.demo.Data.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
//
public interface IOpeningRepository extends MongoRepository<Opening, String>{

    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    Opening findAll(String category);

    @Query("{_id:'?0'}")
    Opening findOpeningByID(String id);

    //query all openings in range
    @Query("{barberUserName:'?0', startTime:{ $gte: ?1, $lte: ?2 }}")
    List<Opening> findOpeningsInRangeByBarber(String barberUsername, LocalDateTime startTime, LocalDateTime endTime);

    @Query(value=" {'barberUserName': ?0 } ", sort = "{'startTime': 1}")
    List<Opening> findOpeningsByBarberUsername(String barberUsername);

    @Query(value = " {'barberUserName': ?0 , 'isAvailable': ?1} ", sort = "{'startTime': 1}")
    List<Opening> findAvailableOpeningsByBarberUsername(String barberUsername,boolean isAvailable);

    @Query(value = " {'isAvailable': ?0} ",sort = "{'startTime': 1}")
    List<Opening> findAllAvailableOpenings(boolean isAvailable);



}
