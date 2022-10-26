package com.microserviceitem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microserviceitem.entities.Item;
import com.microserviceitem.services.ItemService;

@RestController
public class ItemsControllers {

    /* -------------------------------------------------------------------------- */
    /* USO DE QUALIFIER PARA SELECCIONAR EL COMPONENTE QUE VAMOS A UTILIZAR */
    /* -------------------------------------------------------------------------- */
    // Al tener los componentes de feign y rest, debemos elegir con cual de los dos
    // consumimos el api
    // de products
    @Autowired

    private ItemService itemService;

    @GetMapping("/")
    public List<Item> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}/quantity/{quantity}")
    public Item findById(@PathVariable Long id, @PathVariable Integer quantity) {

        return itemService.findById(id, quantity);
    }
}