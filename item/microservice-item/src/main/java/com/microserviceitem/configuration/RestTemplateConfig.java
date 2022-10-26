package com.microserviceitem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// ESTE BEAN CREARA UN CLIENTE QUE TOMARA LOS DATOS DEL MICROSERVICIO PRODUCTS
//  Y LA CONSUMIRA
@Configuration
public class RestTemplateConfig {

    @Bean("clientRest")
    public RestTemplate registerRestTemplate() {

        return new RestTemplate();
    }
}