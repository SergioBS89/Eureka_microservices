package com.microserviceitem.services;

import java.util.List;
import java.util.stream.Collectors;

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

}