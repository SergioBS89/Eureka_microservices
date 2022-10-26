package com.microserviceitem.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.microserviceitem.dao.ItemServiceInterface;
import com.microserviceitem.entities.Item;
import com.microserviceitem.entities.Product;

@Service("serviceRest")
@Primary
public class ItemService implements ItemServiceInterface {

    /* -------------------------------------------------------------------------- */
    /* SERVICIO REST PARA CONSUMIR API DE PRODUCTS */
    /* -------------------------------------------------------------------------- */

    @Autowired
    private RestTemplate template;

    @Override
    public List<Item> findAll() {
        List<Product> product = Arrays.asList(template.getForObject("http://localhost:8081/", Product[].class));

        return product.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        Map<String, String> pathVariables = new HashMap<String, String>();
        pathVariables.put("id", id.toString());
        Product product = template.getForObject("http://localhost:8081/{id}", Product.class, pathVariables);
        return new Item(product, quantity);
    }

}