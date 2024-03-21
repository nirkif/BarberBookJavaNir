package com.example.demo.Repository;
import com.example.demo.Data.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IUserRepository extends MongoRepository<User, String> {

    @Query("{name:'?0'}")
    List<User> findUserByName(String name);

    @Query("{username:'?0'}")
    User findUserByUserName(String username);
    @Query("{id:'?0'}")
    User findUserById(String id);

    @Query("{phoneNum:'?0'}")
    User findUserByPhoneNumber(String phoneNum);

    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
    List<User> findAll(String category);

    public long count();

}