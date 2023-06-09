package com.example.coinProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CoinProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinProjectApplication.class, args);
	}

}
