package com.example.demo;


import com.example.demo.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
public class BarberShopNirApplication{
	@Autowired
	IUserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(BarberShopNirApplication.class, args);
	}

}

