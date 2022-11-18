package com.microserviceitem.services;

import java.util.List;
import java.util.stream.Collectors;

import com.microserviceitem.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microserviceitem.dao.ItemServiceInterface;
import com.microserviceitem.dao.feign.IProductClientFeign;
import com.microserviceitem.entities.Item;

/* -------------------------------------------------------------------------- */
/*                SERVICIO FEIGN PARA CONSUMIR API DE PRODUCTS                */
/* -------------------------------------------------------------------------- */
@Service("serviceFeign")
// @Primary
public class ItemServiceFeign implements ItemServiceInterface {

    // Injectamos el cliente rest
    @Autowired
    private IProductClientFeign clientFeign;

    @Override
    public List<Item> findAll() {

        return clientFeign.findAll().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {

        return new Item(clientFeign.detail(id), quantity);
    }

    @Override
    public Product save(Product product) {
        return clientFeign.save(product);
    }

    @Override
    public Product update(Product product, Long id) {
        return clientFeign.update(product,id);
    }

    @Override
    public void delete(Long id) {
       clientFeign.delete(id);
    }
}