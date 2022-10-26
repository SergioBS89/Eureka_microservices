package com.eureka.serviceeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ServiceEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceEurekaServerApplication.class, args);
	}

}
