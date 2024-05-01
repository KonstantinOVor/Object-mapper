package com.example.Object.mapper.service.impl;

import com.example.Object.mapper.dto.ProductFilterDto;
import com.example.Object.mapper.model.Customer;
import com.example.Object.mapper.model.Order;
import com.example.Object.mapper.model.OrderStatus;
import com.example.Object.mapper.model.Product;
import com.example.Object.mapper.repository.CustomerRepository;
import com.example.Object.mapper.repository.OrderRepository;
import com.example.Object.mapper.repository.ProductRepository;
import com.example.Object.mapper.service.OrderService;
import com.example.Object.mapper.util.JSONConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public List<String> getAllOrders(ProductFilterDto filter) {
        if (filter.getPageNumber() == null || filter.getPageSize() == null) {
            throw new IllegalArgumentException("Параметры фильтрации не могут быть пустыми или отрицательными");
        }
        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(
                filter.getPageNumber(),
                filter.getPageSize()));

        return orderPage.getContent().stream()
                .map(JSONConverter::objectToJSON)
                .collect(Collectors.toList());
    }
    @Override
    public String getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        return JSONConverter.objectToJSON(order);
    }

    @Override
    public Order createOrder(Long customerId, String shippingAddress, String products) {
        validateOrderData(customerId, shippingAddress, products);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не может быть создан из-за неверного Id покупателя"));

        List<Product> productsToOrder = createProductList(products);

        Order order = createOrUpdateOrder(customer, productsToOrder, shippingAddress, OrderStatus.CREATED);

        saveOrder(order, customer.getCustomerId());

        return order;
    }
    private void validateOrderData(Long customerId, String shippingAddress, String products) {
        if (customerId == null || shippingAddress == null || products == null) {
            throw new IllegalArgumentException("Заказ не может быть создан из-за недостающих данных");
        }
    }
    private List<Product> createProductList(String products) {

        List<String> productsList = Arrays.stream(products.split("},?\r\n"))
                .map(product -> product.concat("}"))
                .toList();
        if (productsList.isEmpty()) {
            throw new IllegalArgumentException("Заказ не может быть создан из-за пустого списка продуктов");
        }

        return productsList.stream()
                .map(json -> {
                    Product product = JSONConverter.jsonToObject(json, Product.class);
                    if (productRepository.findById(product.getProductId()).isEmpty()) {
                        try {
                            productRepository.save(product);
                        } catch (DataAccessException e) {
                            throw new RuntimeException("Ошибка при создании продукта: " + e.getMessage());
                        }
                    }
                    return product;
                })
                .toList();
    }
    private Order createOrUpdateOrder(Customer customer, List<Product> productsToOrder,
                                      String shippingAddress, OrderStatus status) {
        Order updatedOrder = orderRepository.findByCustomerAndOrderStatus(customer, status);
        if (updatedOrder == null) {
            return Order.builder()
                    .customer(customer)
                    .products(productsToOrder)
                    .shippingAddress(shippingAddress)
                    .orderDate(LocalDateTime.now())
                    .totalPrice(calculateTotalPrice(productsToOrder))
                    .orderStatus(status)
                    .build();
        } else {
            updatedOrder.setProducts(productsToOrder);
            updatedOrder.setShippingAddress(shippingAddress);
            updatedOrder.setOrderDate(LocalDateTime.now());
            updatedOrder.setTotalPrice(calculateTotalPrice(productsToOrder));
            updatedOrder.setOrderStatus(status);
            }
        return updatedOrder;
        }
    private double calculateTotalPrice(List<Product> products) {
        double totalPrice = 0.0;
        for (Product product : products) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
    private void saveOrder(Order order,  Long customerId) {
        try {
            Customer savedCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("В базе данных не существует покупателя с Id =" + customerId));

            savedCustomer.getOrders().add(order);
            customerRepository.save(savedCustomer);
            orderRepository.saveAndFlush(order);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при создании заказа: " + e.getMessage());
        }
    }

    @Override
    public Order updateOrder(Long orderId, String jsonOrder) {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderId);
        if (existingOrderOptional.isEmpty()) {
            throw new IllegalArgumentException("Заказ не найден");
        }

       if (jsonOrder == null) {
           return existingOrderOptional.get();
       }

       Order order = JSONConverter.jsonToObject(jsonOrder, Order.class);
        if (order == null) {
            throw new IllegalArgumentException("Ошибка при преобразовании JSON в объект Order");
        }

       Order updatedOrder = createOrUpdateOrder(order.getCustomer(), order.getProducts(),
               order.getShippingAddress(), OrderStatus.MODIFIED);

       saveOrder(updatedOrder, updatedOrder.getCustomer().getCustomerId());

       return updatedOrder;
    }
    @Override
    public void deleteOrder(Long id) {
        if(id == null){
            throw new IllegalArgumentException("Id не может быть пустым");
        }
        if(!orderRepository.existsById(id)){
            throw new IllegalArgumentException("Заказ с указанным id не найден");
        }
        try {
            orderRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при удалении заказа: " + e.getMessage());
        }
    }
}
