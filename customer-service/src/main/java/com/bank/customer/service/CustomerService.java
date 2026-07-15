package com.bank.customer.service;


import com.bank.customer.dto.CustomerRequest;
import com.bank.customer.dto.CustomerResponse;
import com.bank.customer.entity.Customer;
import com.bank.customer.exception.CustomerNotFoundException;
import com.bank.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class CustomerService {

   // @Autowired
    //private CustomerRepository customerRepository;
 private final CustomerRepository customerRepository;
public CustomerService(CustomerRepository customerRepository){
    this.customerRepository = customerRepository;
}
//    public List<Customer> getAllCustomers() {
//        return customerRepository.findAll();
//    }
// Get All Customers
   public List<CustomerResponse> getAllCustomers() {

    List<Customer> customers = customerRepository.findAll();

    return customers.stream()
            .map(customer -> new CustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone()))
            .collect(Collectors.toList());
}

//    public Customer saveCustomer(Customer customer) {
//        return customerRepository.save(customer);
//    }
// create customer
    public CustomerResponse saveCustomer(CustomerRequest request) {

        // Convert Request DTO to Entity
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        // Save to database
        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone());

//        // Convert Entity to Response DTO
//        CustomerResponse response = new CustomerResponse();
//        response.setId(savedCustomer.getId());
//        response.setName(savedCustomer.getName());
//        response.setEmail(savedCustomer.getEmail());
//        response.setPhone(savedCustomer.getPhone());
//
//        return response;
    }

    //get customer by id
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone());
//        return customerRepository.findById(id)
//                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }
//update customer
    public CustomerResponse updateCustomer(Long id, CustomerRequest  request ) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        existingCustomer.setName(request.getName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());

        existingCustomer.setName(request.getName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());

        Customer savedCustomer = customerRepository.save(existingCustomer);

        return new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone());
    }
//delete customer
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customerRepository.delete(customer);
    }
}