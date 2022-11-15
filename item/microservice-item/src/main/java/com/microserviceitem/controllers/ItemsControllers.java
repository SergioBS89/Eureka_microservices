package com.microserviceitem.controllers;

import java.util.List;

import com.microserviceitem.services.ItemService;
import com.microserviceitem.services.ItemServiceFeign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microserviceitem.entities.Item;
import com.microserviceitem.entities.Product;

@RestController
public class ItemsControllers {

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private ItemService itemService;
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
        // the
        // Info of the product showed below )
        return circuitBreakerFactory.create("items")
                .run(() -> itemServiceFeign.findById(id, q), error -> alternativeError(id,
                        q));

    }

    public Item alternativeError(Long id, Integer quan) {
        Item i = new Item();
        Product p = new Product();

        i.setQuantity(quan);
        p.setId(id);
        p.setName("Tv sony Bravia, 4k, 60'' method alternative with resilience4j3");
        p.setPrice(2500.00);
        i.setProduct(p);
        return i;
    }

    @CircuitBreaker(name = "items") // Indicate the name of config in properties
    @GetMapping("/{id}/q/{q}")
    public Item findByIdWithNotations(@PathVariable Long id, @PathVariable Integer q) {

        return itemServiceFeign.findById(id, q);
    }
}