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
                            .slidingWindowSize(4)
                            // total oportunities 50%
                            .failureRateThreshold(50)
                            // wait time open
                            .waitDurationInOpenState(Duration.ofSeconds(10L))
                            // num of request in half open (more info in definitions.txt)
                            .permittedNumberOfCallsInHalfOpenState(3)
                            /* ------------------ THIS CONFIG IS MANNAGED IN PROPERTIES ----------------- */
                            // Properties config has priority over this bean...so this configuration does
                            // not working
                            // total oportunities 50%
                            .slowCallRateThreshold(50)
                            // Attemps that duration is more than 2 seconds are counted as slowcalls
                            // (so, if we have more than )
                            .slowCallDurationThreshold(Duration.ofSeconds(5L))
                            .build())

                    // Configuration to do slow calls (6 seconds) and enable to count slow calls
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6L)).build())
                    .build();
        });
    }
}