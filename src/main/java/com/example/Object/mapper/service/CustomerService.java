package com.example.Object.mapper.service;

import com.example.Object.mapper.model.Customer;

import java.util.List;

public interface CustomerService {

    void addCustomer(Customer customer);
    Customer getCustomerById(Long customerId);
    List<Customer> getAllCustomers();
    void updateCustomer(Customer customer);
    void deleteCustomer(Long customerId);
}
