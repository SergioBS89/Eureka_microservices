package com.microserviceitem.configuration;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

// ESTE BEAN CREARA UN CLIENTE QUE TOMARA LOS DATOS DEL MICROSERVICIO PRODUCTS
//  Y LA CONSUMIRA
@Configuration
public class RestTemplateConfig {

    @Bean("clientRest")
    public RestTemplate registerRestTemplate() {

        return new RestTemplate();
    }

    /* -------------------------------------------------------------------------- */
    /* CUSTOMIZING resilience4j to try in method find by id */
    /* -------------------------------------------------------------------------- */
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {

        return factory -> factory.configureDefault(id -> {

            return new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.custom()
                            // num of request in total state (more info in definitons.txt)
                            .slidingWindowSize(10)
                            // total oportunities
                            .failureRateThreshold(50)
                            // wait time open
                            .waitDurationInOpenState(Duration.ofSeconds(10L))
                            // num of request in half open (more info in definitions.txt)
                            .permittedNumberOfCallsInHalfOpenState(3)
                            .build())
                    .timeLimiterConfig(TimeLimiterConfig.ofDefaults())
                    .build();
        });
    }
}