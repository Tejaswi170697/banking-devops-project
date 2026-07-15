package com.bank.customer.service;


import com.bank.customer.entity.Customer;
import com.bank.customer.exception.CustomerNotFoundException;
import com.bank.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customerRepository.delete(customer);
    }
}