package com.microserviceitem.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.microserviceitem.dao.ItemServiceInterface;
import com.microserviceitem.entities.Item;
import com.microserviceitem.entities.Product;

@Service("serviceRest")
@Primary
public class ItemRestService implements ItemServiceInterface {

    /* -------------------------------------------------------------------------- */
    /* SERVICIO REST PARA CONSUMIR API DE PRODUCTS */
    /* -------------------------------------------------------------------------- */
    /*Este archivo toma los datos enviados desde un api rest para mostrar los datos */
    @Autowired
    private RestTemplate restClient;

    @Override
    public List<Item> findAll() {
        List<Product> product = Arrays.asList(restClient.getForObject("http://localhost:8081/", Product[].class));
//        List<Product> product = Arrays.asList(Objects.requireNonNull(restClient.getForObject("http://localhost:8081/", Product[].class)));

        return product.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        Map<String, String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id", id.toString());
        Product product = restClient.getForObject("http://localhost:8081/{id}", Product.class, pathVariables);
        return new Item(product, quantity);
    }

    @Override
    public Product save(Product product) {
        HttpEntity<Product> body = new HttpEntity<Product>(product);
        ResponseEntity<Product> response = restClient.exchange("http://microservice-products/", HttpMethod.POST, body, Product.class);
        return response.getBody();
    }

    @Override
    public Product update(Product product, Long id) {
        //Tomamos el valor del id con el map
        Map<String, String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id", id.toString());
        //Nuevo objeto Http  del tipo Product
        HttpEntity<Product> body = new HttpEntity<Product>(product);
        //Creamos el objeto con los datos que se pasan
        ResponseEntity<Product> response = restClient.exchange("http://microservice-products/{id}",
                HttpMethod.PUT, body, Product.class, pathVariables);
        return response.getBody();
    }

    @Override
    public void delete(Long id) {
        //Tomamos el valor del id con el map
        Map<String, String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id", id.toString());
        restClient.delete("http://microservice-products/{id}", pathVariables);
    }

}