package com.microserviceproducts.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    // ----------SIMULATING ERRORS TO THROW THE CIRCUIT BREAKER IN ITEM MICROSERVICE--------
    @GetMapping("/{id}")
    public Product detail(@PathVariable Long id) {

        // Simulating an error
        if (id.equals(10L)) {
            throw new IllegalStateException("Product not found...please try again");
        }

        if (id.equals(7L)) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Product p = productService.findById(id);
        // Asignation random port
        p.setPort(Integer.parseInt(environment.getProperty("local.server.port")));

        return p;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@RequestBody Product product){
        return productService.save(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product update(@RequestBody Product product, @PathVariable Long id){
        Product p = productService.findById(id);
        product.setName(p.getName());
        product.setPrice(p.getPrice());
        return productService.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        productService.delete(id);
    }
}