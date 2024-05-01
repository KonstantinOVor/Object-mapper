package com.example.Object.mapper.repository;

import com.example.Object.mapper.model.Customer;
import com.example.Object.mapper.model.Order;
import com.example.Object.mapper.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByCustomerAndOrderStatus(Customer customer, OrderStatus orderStatus);
}
