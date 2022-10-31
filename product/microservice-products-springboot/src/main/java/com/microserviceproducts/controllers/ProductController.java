package com.microserviceproducts.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microserviceproducts.models.entities.Product;
import com.microserviceproducts.models.services.ProductService;

//Retorna el json 
@RestController
public class ProductController {

    /* -------------------------------------------------------------------------- */
    /* INYECCION DEL NUMERO DE PUERTO QUE ESTAMOS USANDO */
    /* -------------------------------------------------------------------------- */

    @Autowired
    private Environment environment;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public List<Product> listOfProducts() {
        // Assignation random port to every product
        return productService.findAll().stream().map(s -> {
            s.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return s;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product detail(@PathVariable Long id) {

        // Simulating an error
        if (id.equals(10L)) {
            throw new IllegalStateException("Product not found...please try again");
        }

        if (id.equals(7L)) {
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Product p = productService.findById(id);
        // Asignation random port
        p.setPort(Integer.parseInt(environment.getProperty("local.server.port")));

        return p;

    }
}