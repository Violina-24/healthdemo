package com.health.healthdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.health.healthdemo")
public class HealthdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthdemoApplication.class, args);
	}

}
