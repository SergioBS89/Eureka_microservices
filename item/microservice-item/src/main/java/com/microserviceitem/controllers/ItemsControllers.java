package com.microserviceitem.controllers;

import java.util.List;

import com.microserviceitem.services.ItemService;
import com.microserviceitem.services.ItemServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.microserviceitem.entities.Item;


@RestController
public class ItemsControllers {

    /* -------------------------------------------------------------------------- */
    /* USO DE QUALIFIER PARA SELECCIONAR EL COMPONENTE QUE VAMOS A UTILIZAR */
    /* -------------------------------------------------------------------------- */
    // Al tener los componentes de feign y rest, debemos elegir con cual de los dos
    // consumimos el api
    // de products

//     Servicio item
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @GetMapping("/")
    public List<Item> findAll() {
        return itemServiceFeign.findAll();
    }

    @GetMapping("/{id}/quantity/{quantity}")
    public Item findById(@PathVariable Long id, @PathVariable Integer quantity) {

        return itemServiceFeign.findById(id, quantity);
    }
}