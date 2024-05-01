package com.example.Object.mapper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank(message = "Название продукта не может быть пустым")
    private String name;
    private String description;
    @Min(value = 0, message = "Цена должна быть больше или равна нулю")
    private double price;
    @Column(name = "quantity_in_stock")
    private int quantityInStock;
}

