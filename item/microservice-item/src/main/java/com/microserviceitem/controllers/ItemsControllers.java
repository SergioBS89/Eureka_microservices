package com.microserviceitem.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import com.microserviceitem.services.ItemService;
import com.microserviceitem.services.ItemServiceFeign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microserviceitem.entities.Item;
import com.microserviceitem.entities.Product;

@RestController
public class ItemsControllers {

    private final Logger logger = LoggerFactory.getLogger(ItemsControllers.class);

    //Injection Bean of environment to know wich properties is used in git repo
    @Autowired
    private Environment env;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    //Value to get the value text of file.properties from Git Repository inside Global_config_server (save in Lenovo ubuntu / proyects)
    @Value("${config.text}")
    private String textGit;
    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @GetMapping("/")
    public List<Item> findAll() {
        return itemServiceFeign.findAll();
    }

    @GetMapping("/{id}/quan/{q}")
    public Item findById(@PathVariable Long id, @PathVariable Integer q) {

        /* ------------------ SIMULATING FAILS WITH CIRCUIT BREAKER ----------------- */
        // Return a expression lambda with an object of Circuit breaker to implement
        // resiliance4j by default

        // (If the program fails many times (by default 50% of 100 request), it trow
        // the circuit breaker...after that, the second method error is executed showing
        // the Info of the product showed below )
        return circuitBreakerFactory.create("items")
                .run(() -> itemServiceFeign.findById(id , q));
                // ,error -> alternativeError(id,
                //        q));

    }

    public Item alternativeError(Long id, Integer quan, Throwable e) {
        logger.info(e.getMessage());
        Item i = new Item();
        Product p = new Product();

        i.setQuantity(quan);
        p.setId(id);
        p.setName("Tv sony Bravia, 4k, 60'' method alternative with resilience4j3");
        p.setPrice(2500.00);
        i.setProduct(p);
        return i;
    }
    
    // WORKING WITH ANOTATIONS AND .YML CONFIG (It doesnt work well...works with the values from products controllers)
    @CircuitBreaker(name = "items") // Indicate the name of config in properties
    @GetMapping("/{id}/q/{q}")
    public Item findByIdWithNotations(@PathVariable Long id, @PathVariable Integer q) {
        return itemServiceFeign.findById(id, q);
    }

//    THIS HANDLER SHOWS A JSON WITH THE PROPERTIES TEXT FROM GIT AND THE NUMBER OF THE PORT USED
    @GetMapping("/get-conf")
    public ResponseEntity<Map<String,String>> getConfigurationFromGit(@Value("${server.port}") String numPort){
       //Logger shows in the console the value
        logger.info(textGit);

        Map<String,String> json = new HashMap<>();
        json.put("Text", textGit);
        json.put("Port", numPort);

        //Asking if the object environment exist
        if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")){
            json.put("Author.name", env.getProperty("config.author.name"));
            json.put("Author.email", env.getProperty("config.author.email"));
        }

        return new ResponseEntity<Map<String,String>>(json, HttpStatus.OK);
    }
}