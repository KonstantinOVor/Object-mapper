package com.example.Object.mapper.controllers;

import com.example.Object.mapper.dto.ProductFilterDto;
import com.example.Object.mapper.model.Product;
import com.example.Object.mapper.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public ResponseEntity<List<String>> getAllProducts(ProductFilterDto filter) {
        return ResponseEntity.ok(productService.getAllProducts(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) {
        String product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok().body(product) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody String jsonProduct) {
        return ResponseEntity.ok().body(productService.createProduct(jsonProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody String jsonProduct) {
        Product product = productService.updateProduct(id, jsonProduct);
        return product != null ? ResponseEntity.ok().body(product) : ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
