package com.example.Object.mapper.service.impl;

import com.example.Object.mapper.dto.ProductFilterDto;
import com.example.Object.mapper.model.Product;
import com.example.Object.mapper.repository.ProductRepository;
import com.example.Object.mapper.service.ProductService;
import com.example.Object.mapper.util.JSONConverter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Value("${app.numberOfProducts}")
    private int numberOfProducts;

    @PostConstruct
    @Lazy
    public void initProducts() {

        List<Product> products = IntStream.rangeClosed(1, numberOfProducts)
                .mapToObj(i -> Product.builder()
                        .name("Product " + i)
                        .description("Description " + i)
                        .price(10 * i)
                        .quantityInStock(100 * i)
                        .build())
                .toList();
        productRepository.saveAll(products);
    }

    @Override
    public Product createProduct(String jsonProduct) {
        if (jsonProduct == null) {
            throw new IllegalArgumentException("Продукт не может быть пустым");
        }

        Product product = JSONConverter.jsonToObject(jsonProduct, Product.class);
        if (product == null) {
            throw new IllegalArgumentException("Ошибка при преобразовании JSON в объект Product");
        }
        try {
            return productRepository.save(product);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при создании продукта: " + e.getMessage());
        }
    }

    @Override
    public String getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден"));
        return JSONConverter.objectToJSON(product);
    }

    @Override
    public List<String> getAllProducts(ProductFilterDto filter) {
        if (filter.getPageNumber() == null || filter.getPageSize() == null ||
                (filter.getPageNumber() < 0) || filter.getPageSize() <= 0) {
            throw new IllegalArgumentException("Параметры фильтрации не могут быть пустыми или отрицательными");
        }
        Page<Product> productPage = productRepository.findAll(PageRequest.of(
                filter.getPageNumber(),
                filter.getPageSize()));

        return productPage.getContent().stream()
                .map(JSONConverter::objectToJSON)
                .toList();

    }

    @Override
    public Product updateProduct(Long productId, String jsonProduct) {
        Optional<Product> existingProductOptional = productRepository.findById(productId);
        if (existingProductOptional.isEmpty()) {
            throw new IllegalArgumentException("Продукт не найден");
        }
        if (jsonProduct == null) {
            return existingProductOptional.get();
        }

        Product product = JSONConverter.jsonToObject(jsonProduct, Product.class);
        if (product == null) {
            throw new IllegalArgumentException("Ошибка при преобразовании JSON в объект Product");
        }

        Product updatedProduct = createProduct(existingProductOptional, product);

        try {
            return productRepository.save(updatedProduct);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при обновлении продукта: " + e.getMessage());
        }
    }

    private Product createProduct(Optional<Product> existingProductOptional, Product product) {
        Product updatedProduct = existingProductOptional.orElse(new Product());
        updatedProduct.setName(product.getName());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setQuantityInStock(product.getQuantityInStock());
        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long productId) {
        if(productId == null){
            throw new IllegalArgumentException("Id не может быть пустым");
        }

        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Продукт с указанным id не найдена");
        }

        try {
            productRepository.deleteById(productId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при удалении продукта: " + e.getMessage());
        }
    }
}
