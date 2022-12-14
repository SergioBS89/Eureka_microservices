package com.microserviceitem.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microserviceitem.services.ItemRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import com.microserviceitem.services.ItemService;
import com.microserviceitem.services.ItemServiceFeign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microserviceitem.entities.Item;
import com.microserviceitem.entities.Product;

@RestController
@RefreshScope
//It is used to refresh the data of git config in real time, so if data change in git, change in the app at the
//same time. Refreshing dates that takes the annotation @Value("$config.text") shows below
public class ItemsControllers {


    //------------------------------------------------------------------------------------------------------------//
    /*HAVING TWO SERVICES (REST-CLIENT AND FEING) AND ONLY ONE CONTROLLER, ITS NECESSARY TO SWITCH BETWEEN BOTH.
    TO DO THAT EASY, JUST CHANGE TRUE OR FALSE THE NEXT VARIABLE
    In this case, it's needed to use feign as client because the microservice products has a dinamic port, so its
    impossible working with restClient, it uses a static port 8081 */
    boolean isRestClient = false;
    @Autowired
    private ItemServiceFeign itemServiceFeign;
    @Autowired
    private ItemRestService itemRestService;
//-------------------------------------------------------------------------------------------------------------//

    //Value to get the value text of file.properties from Git Repository inside Global_config_server (save in Lenovo ubuntu / proyects)
    @Value("${config.text}")
    private String textGit;

    //This object is to get information about how works circuit breaker in the console
    private final Logger logger = LoggerFactory.getLogger(ItemsControllers.class);


    @Autowired //Inject a Bean of environment to know which properties is used in git repo and print the info as json
    private Environment env;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;


    //     ------------------------METHODS----------------------------
    @GetMapping("/")
    public List<Item> findAll() {
        if (isRestClient) {
            return itemRestService.findAll();
        } else {
            return itemServiceFeign.findAll();
        }

    }

    /* ------------------ SIMULATING FAILS WITH CIRCUIT BREAKER ----------------- */
         /*Return a expression lambda with an object of Circuit breaker to implement resilience by default
         (If the program fails many times (managed in the configuration), it throws the circuit breaker...after that,
          the second method error is executed showing the Info of the product showed below )*/
    @GetMapping("/{id}/quan/{q}")
    public Item findById(@PathVariable Long id, @PathVariable Integer q) {

        if (isRestClient) {
            return circuitBreakerFactory.create("items")
                    .run(() -> itemRestService.findById(id, q));
            // ,error -> alternativeError(id,
            //        q));
        } else {
            return circuitBreakerFactory.create("items")
                    .run(() -> itemServiceFeign.findById(id, q));
            // ,error -> alternativeError(id,
            //        q));
        }
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

    /*    WORKING WITH ANNOTATIONS AND .YML CONFIG (It doesn't work well...works with the values from products controllers)*/
    @CircuitBreaker(name = "items") // Indicate the name of config in properties
    @GetMapping("/{id}/q/{q}")
    public Item findByIdWithNotations(@PathVariable Long id, @PathVariable Integer q) {
        if (isRestClient) {
            return itemRestService.findById(id, q);
        } else {
            return itemServiceFeign.findById(id, q);
        }
    }

    //    THIS HANDLER SHOWS A JSON WITH THE PROPERTIES TEXT FROM GIT AND THE NUMBER OF THE PORT USED
    @GetMapping("/get-conf")
    public ResponseEntity<Map<String, String>> getConfigurationFromGit(@Value("${server.port}") String numPort) {
        //Logger shows in the console the value
        logger.info(textGit);
        Map<String, String> json = new HashMap<>();
        json.put("Text", textGit);
        json.put("Port", numPort);

        //Asking if the object environment exist
        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("Author.name", env.getProperty("config.author.name"));
            json.put("Author.email", env.getProperty("config.author.email"));
        }
        return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@RequestBody Product product) {
        if (isRestClient) {
            return itemRestService.save(product);
        } else {
            return itemServiceFeign.save(product);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product update(@RequestBody Product product, @PathVariable Long id) {
        if (isRestClient) {
            return itemRestService.update(product, id);
        } else {
            return itemServiceFeign.update(product, id);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (isRestClient) {
            itemRestService.delete(id);
        } else {
            itemServiceFeign.delete(id);
        }
    }
}