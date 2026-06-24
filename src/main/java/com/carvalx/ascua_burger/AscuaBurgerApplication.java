package com.carvalx.ascua_burger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AscuaBurgerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AscuaBurgerApplication.class, args);
	}
}