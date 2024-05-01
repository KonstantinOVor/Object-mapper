package com.example.Object.mapper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToMany
    private List<Product> products;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "shipping_address")
    @NotNull(message = "Адрес доставки не может быть пустым")
    private String shippingAddress;
    @Column(name = "total_price")
    @Min(value = 0, message = "Сумма заказа должна быть больше или равна нулю")
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
}
