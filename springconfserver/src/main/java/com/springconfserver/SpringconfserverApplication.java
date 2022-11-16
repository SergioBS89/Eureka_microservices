package com.springconfserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SpringconfserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringconfserverApplication.class, args);
	}
}
