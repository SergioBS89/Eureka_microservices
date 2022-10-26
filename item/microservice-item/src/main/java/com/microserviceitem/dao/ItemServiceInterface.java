package com.microserviceitem.dao;

import java.util.List;

import com.microserviceitem.entities.Item;

public interface ItemServiceInterface {

    public List<Item> findAll();

    public Item findById(Long id, Integer quantity);
}