package com.example.Object.mapper.service.impl;

import com.example.Object.mapper.model.Customer;
import com.example.Object.mapper.repository.CustomerRepository;
import com.example.Object.mapper.service.CustomerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Value("${app.customers}")
    private int numberOfCustomers;

    @PostConstruct
    public void initCustomer() {

        List<Customer> customers = IntStream.rangeClosed(1, numberOfCustomers)
                .mapToObj(i -> Customer.builder()
                        .firstName("Customer " + i)
                        .lastName("LastName " + i)
                        .email("Email@" + i+ ".com")
                        .contactNumber("Contact " + i)
                        .build())
                .toList();
        customerRepository.saveAll(customers);
    }
    @Override
    public void addCustomer(Customer customer) {

    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return null;
    }

    @Override
    public void updateCustomer(Customer customer) {

    }

    @Override
    public void deleteCustomer(Long customerId) {

    }
}
