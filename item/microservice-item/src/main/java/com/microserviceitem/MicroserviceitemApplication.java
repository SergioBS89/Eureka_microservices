package com.microserviceitem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Habilita la inyeccion de clientes feign de netflix(cliente htpp)
@EnableEurekaClient // Habilita la inyeccion de clientes eureka
public class MicroserviceitemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceitemApplication.class, args);
	}

}
