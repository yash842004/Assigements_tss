package com.tss.banking.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tss.banking.dto.request.CustomerRegistrationRequestDTO;
import com.tss.banking.dto.request.CustomerUpdateRequestDTO;
import com.tss.banking.dto.request.PasswordChangeRequestDTO;
import com.tss.banking.dto.response.CustomerResponseDTO;
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.eums.CustomerStatus;

/**
 * Service interface for customer management
 */
public interface CustomerService {
    
    /**
     * Register a new customer
     * @param registrationRequest Customer registration details
     * @return Created customer response
     */
    CustomerResponseDTO registerCustomer(CustomerRegistrationRequestDTO registrationRequest);
    
    /**
     * Update customer information
     * @param customerId Customer ID
     * @param updateRequest Updated customer details
     * @return Updated customer response
     */
    CustomerResponseDTO updateCustomer(Long customerId, CustomerUpdateRequestDTO updateRequest);
    
    /**
     * Get customer by ID
     * @param customerId Customer ID
     * @return Customer response
     */
    CustomerResponseDTO getCustomerById(Long customerId);
    
    /**
     * Get customer by email
     * @param email Customer email
     * @return Customer response
     */
    CustomerResponseDTO getCustomerByEmail(String email);
    
    /**
     * Get all customers with pagination
     * @param pageable Pagination information
     * @return Page of customers
     */
    Page<CustomerResponseDTO> getAllCustomers(Pageable pageable);
    
    /**
     * Get customers by status
     * @param status Customer status
     * @param pageable Pagination information
     * @return Page of customers
     */
    Page<CustomerResponseDTO> getCustomersByStatus(CustomerStatus status, Pageable pageable);
    
    /**
     * Search customers by name or email
     * @param searchTerm Search term
     * @param pageable Pagination information
     * @return Page of customers
     */
    Page<CustomerResponseDTO> searchCustomers(String searchTerm, Pageable pageable);
    
    /**
     * Change customer password
     * @param customerId Customer ID
     * @param passwordChangeRequest Password change details
     */
    void changePassword(Long customerId, PasswordChangeRequestDTO passwordChangeRequest);
    
    /**
     * Update customer status
     * @param customerId Customer ID
     * @param status New status
     * @return Updated customer response
     */
    CustomerResponseDTO updateCustomerStatus(Long customerId, CustomerStatus status);
    
    /**
     * Activate customer account
     * @param customerId Customer ID
     * @return Updated customer response
     */
    CustomerResponseDTO activateCustomer(Long customerId);
    
    /**
     * Deactivate customer account
     * @param customerId Customer ID
     * @return Updated customer response
     */
    CustomerResponseDTO deactivateCustomer(Long customerId);
    
    /**
     * Suspend customer account
     * @param customerId Customer ID
     * @return Updated customer response
     */
    CustomerResponseDTO suspendCustomer(Long customerId);
    
    /**
     * Check if customer exists by email
     * @param email Customer email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if customer is active
     * @param customerId Customer ID
     * @return true if active, false otherwise
     */
    boolean isCustomerActive(Long customerId);
    
    /**
     * Get customer entity by ID (for internal use)
     * @param customerId Customer ID
     * @return Customer entity
     */
    Customer getCustomerEntityById(Long customerId);
    
    /**
     * Get customer entity by email (for internal use)
     * @param email Customer email
     * @return Customer entity
     */
    Optional<Customer> getCustomerEntityByEmail(String email);
    
    /**
     * Validate customer credentials
     * @param email Customer email
     * @param password Customer password
     * @return Customer entity if valid, empty optional otherwise
     */
    Optional<Customer> validateCustomerCredentials(String email, String password);
    
    /**
     * Get customer's total account count
     * @param customerId Customer ID
     * @return Number of accounts
     */
    int getCustomerAccountCount(Long customerId);
    
    /**
     * Delete customer (admin only)
     * @param customerId Customer ID
     */
    void deleteCustomer(Long customerId);
}
