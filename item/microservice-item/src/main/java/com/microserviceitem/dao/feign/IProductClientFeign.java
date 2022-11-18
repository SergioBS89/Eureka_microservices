package com.microserviceitem.dao.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.microserviceitem.entities.Product;

/* -------------------------------------------------------------------------- */
/*                      CLIENTE REST CON FEIGN DE NETFLIX                     */
/* -------------------------------------------------------------------------- */

//Indicamos el nombre del microservicio que vamos a consumir 
@FeignClient(name = "microservice-products")
public interface IProductClientFeign {

    //    FEIGN CLIENT NEEDS TO INIDICATE THE GET MAPPING IN ITS INTERFACE
    @GetMapping("/")
    public List<Product> findAll();

    @GetMapping("/{id}")
    public Product detail(@PathVariable Long id);

    @PostMapping("/")
    public Product save(@RequestBody Product p);

    @PutMapping("/{id}")
    public Product update(@RequestBody Product p, @PathVariable Long id);

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id);
}