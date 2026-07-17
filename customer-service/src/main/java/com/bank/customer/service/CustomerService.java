package com.bank.customer.service;

import com.bank.customer.dto.AccountResponse;
import com.bank.customer.dto.AddressResponse;
import com.bank.customer.dto.CustomerRequest;
import com.bank.customer.dto.CustomerResponse;
import com.bank.customer.entity.Account;
import com.bank.customer.entity.Address;
import com.bank.customer.entity.Customer;
import com.bank.customer.exception.CustomerNotFoundException;
import com.bank.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Get All Customers
    public List<CustomerResponse> getAllCustomers() {
        logger.info("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        logger.info("Total customers found: {}", customers.size());
        return customers.stream()
                .map(this::mapToResponse)

//                .map(customer -> CustomerResponse.builder()
//                        .id(customer.getId())
//                        .name(customer.getName())
//                        .email(customer.getEmail())
//                        .phone(customer.getPhone())
//                        .build())
                .collect(Collectors.toList());
    }

    // Create Customer
    public CustomerResponse saveCustomer(CustomerRequest request) {
        Address address = Address.builder()
                .city(request.getAddress().getCity())
                .state(request.getAddress().getState())
                .build();
        logger.info("Creating customer with email: {}", request.getEmail());
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(address)
                .build();

        List<Account> accounts = new ArrayList<>();

        if (request.getAccounts() != null) {

            accounts = request.getAccounts()
                    .stream()
                    .map(accountRequest -> {

                        Account account = Account.builder()
                                .accountNumber(accountRequest.getAccountNumber())
                                .balance(accountRequest.getBalance())
                                .build();

                        account.setCustomer(customer);

                        return account;

                    })
                    .toList();
        }

        customer.setAccounts(accounts);

        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Customer created successfully with id: {}", savedCustomer.getId());
        return mapToResponse(savedCustomer);

//        return CustomerResponse.builder()
//                .id(savedCustomer.getId())
//                .name(savedCustomer.getName())
//                .email(savedCustomer.getEmail())
//                .phone(savedCustomer.getPhone())
//
//                .build();
    }

    // Get Customer By Id
    public CustomerResponse getCustomerById(Long id) {
        logger.info("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with id: {}", id);
                    return new CustomerNotFoundException("Customer not found");
                });

        logger.info("Customer found with id: {}", id);

        return mapToResponse(customer);
//        return CustomerResponse.builder()
//                .id(customer.getId())
//                .name(customer.getName())
//                .email(customer.getEmail())
//                .phone(customer.getPhone())
//                .build();
    }

    // Update Customer
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        logger.info("Updating customer with id: {}", id);
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with id: {}", id);
                    return new CustomerNotFoundException("Customer not found");
                });

        existingCustomer.setName(request.getName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());

        Customer savedCustomer = customerRepository.save(existingCustomer);
        logger.info("Customer updated successfully with id: {}", savedCustomer.getId());
        return mapToResponse(savedCustomer);
//        return CustomerResponse.builder()
//                .id(savedCustomer.getId())
//                .name(savedCustomer.getName())
//                .email(savedCustomer.getEmail())
//                .phone(savedCustomer.getPhone())
//                .build();
    }

    // Delete Customer
    public void deleteCustomer(Long id) {

        logger.info("Deleting customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with id: {}", id);
                    return new CustomerNotFoundException("Customer not found");
                });

        customerRepository.delete(customer);

        logger.info("Customer deleted successfully with id: {}", id);
    }
    private CustomerResponse mapToResponse(Customer customer) {

        List<AccountResponse> accountResponses = customer.getAccounts()
                .stream()
                .map(account -> AccountResponse.builder()
                        .id(account.getId())
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .build())
                .toList();

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(
                        AddressResponse.builder()
                                .id(customer.getAddress().getId())
                                .city(customer.getAddress().getCity())
                                .state(customer.getAddress().getState())
                                .build()
                )
                .accounts(accountResponses)
                .build();
    }

//    public Page<CustomerResponse> getCustomers(int page, int size) {
//        logger.info("Fetching customers - page: {}, size: {}", page, size);
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<Customer> customerPage = customerRepository.findAll(pageable);
//        logger.info("Customers fetched successfully. Total elements: {}",
//                customerPage.getTotalElements());
//        return customerPage.map(this::mapToResponse);
//    }

    public Page<CustomerResponse> getCustomers(int page,
                                               int size,
                                               String sortBy,
                                               String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        logger.info("Fetching customers - page: {}, size: {}, sortBy: {}, direction: {}",
                page, size, sortBy, direction);

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        logger.info("Customers fetched successfully. Total elements: {}",
                customerPage.getTotalElements());

        return customerPage.map(this::mapToResponse);
    }
    public List<CustomerResponse> searchCustomersByName(String name) {

        logger.info("Searching customers with name containing: {}", name);

        List<Customer> customers = customerRepository.findByNameContaining(name);

        logger.info("Found {} customer(s)", customers.size());

        return customers.stream()
                .map(this::mapToResponse)
                .toList();
    }
    public CustomerResponse getCustomerByEmail(String email) {

        logger.info("Searching customer by email: {}", email);

        Customer customer = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {}", email);
                    return new CustomerNotFoundException("Customer not found with email: " + email);
                });

        logger.info("Customer found with email: {}", email);

        return mapToResponse(customer);
    }
}