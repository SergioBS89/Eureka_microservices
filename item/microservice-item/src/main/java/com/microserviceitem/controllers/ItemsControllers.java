package com.microserviceitem.controllers;

import java.util.List;

import com.microserviceitem.services.ItemService;
import com.microserviceitem.services.ItemServiceFeign;
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

        // (If the program fail some many times (by default 50% of 100 request), it trow
        // the circuit breaker...after that, the second method error is executed)
        return circuitBreakerFactory.create("item")
                .run(() -> itemServiceFeign.findById(id, q), error -> alternativeError(id,
                        q));

    }

    public Item alternativeError(Long id, Integer quan) {

        Item i = new Item();
        Product p = new Product();

        i.setQuantity(quan);
        p.setId(id);
        p.setName("Tv sony Bravia, 4k, 60'' method alternative with resilience4j");
        p.setPrice(2500.00);
        i.setProduct(p);
        return i;
    }
}