package com.toholanka.inventorybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.toholanka.inventorybackend", "com.toholanka.inventorybackend.config"})
public class InventoryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryBackendApplication.class, args);
	}

}
