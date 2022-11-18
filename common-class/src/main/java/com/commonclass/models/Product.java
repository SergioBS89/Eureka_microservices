package com.commonclass.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE) // Add current date
    private Date createdAt;

    @Transient // Indecamos a la base de datos que este atributo no es persistente, no esta en
               // la base de datos
    private Integer port;

}