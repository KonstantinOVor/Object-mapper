package com.example.Object.mapper.service;

import com.example.Object.mapper.dto.ProductFilterDto;
import com.example.Object.mapper.model.Order;
import java.util.List;

public interface OrderService {

    Order createOrder(Long customerId, String shippingAddress, String products);
    String getOrderById(Long orderId);
    List<String> getAllOrders(ProductFilterDto filter);
    Order updateOrder(Long id, String order);
    void deleteOrder(Long id);
}
