package com.example.Object.mapper.controllers;

import com.example.Object.mapper.dto.ProductFilterDto;
import com.example.Object.mapper.model.Order;
import com.example.Object.mapper.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<String>> getAllOrders(ProductFilterDto filter) {
        return ResponseEntity.ok().body(orderService.getAllOrders(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) {
        String order = orderService.getOrderById(id);
        return order != null ? ResponseEntity.ok().body(order) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(Long customerId, String shippingAddress, @RequestBody String jsonProduct) {
        return ResponseEntity.ok().body(orderService.createOrder(customerId, shippingAddress, jsonProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody String jsonOrder) {
        Order order = orderService.updateOrder(id, jsonOrder);
        return order != null ? ResponseEntity.ok().body(order) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
