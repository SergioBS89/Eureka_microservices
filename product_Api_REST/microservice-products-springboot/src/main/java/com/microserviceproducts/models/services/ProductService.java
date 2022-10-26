package com.microserviceproducts.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.microserviceproducts.models.entities.Product;
import com.microserviceproducts.models.repositories.ProductsJpaRepository;

@Service
public class ProductService {

    @Autowired
    private ProductsJpaRepository jpaRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {

        return (List<Product>) jpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {

        return jpaRepository.findById(id).orElse(null);
    }

}