package com.microserviceitem.dao;

import java.util.List;

import com.microserviceitem.entities.Item;
import com.microserviceitem.entities.Product;

public interface ItemServiceInterface {

    public List<Item> findAll();

    public Item findById(Long id, Integer quantity);

    public Product save(Product product);

    public Product update(Product product, Long id);

    public  void delete(Long id);
}