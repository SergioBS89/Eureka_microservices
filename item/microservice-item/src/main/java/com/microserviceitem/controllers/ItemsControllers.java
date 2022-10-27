package com.microserviceitem.controllers;

import java.util.List;

import com.microserviceitem.services.ItemService;
import com.microserviceitem.services.ItemServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microserviceitem.entities.Item;

@RestController
public class ItemsControllers {

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

        return itemServiceFeign.findById(id, q);
    }
}