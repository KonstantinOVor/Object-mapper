package com.example.Object.mapper.service;

import com.example.Object.mapper.dto.ProductFilterDto;
import com.example.Object.mapper.model.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(String jsonProduct);

    String getProductById(Long productId);

    List<String> getAllProducts(ProductFilterDto productFilterDto);

    Product updateProduct(Long productId, String jsonProduct);

    void deleteProduct(Long productId);
}
