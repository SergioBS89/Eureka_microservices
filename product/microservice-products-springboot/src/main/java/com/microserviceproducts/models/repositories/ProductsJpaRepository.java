package com.microserviceproducts.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microserviceproducts.models.entities.Product;

public interface ProductsJpaRepository extends JpaRepository<Product, Long> {

}
