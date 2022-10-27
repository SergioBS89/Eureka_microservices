package com.microserviceproducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroserviceProductsSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProductsSpringbootApplication.class, args);
	}

}
