package com.tss.banking.service.impl;

import com.tss.banking.dto.request.CustomerRegistrationRequestDTO;
import com.tss.banking.dto.request.CustomerUpdateRequestDTO;
import com.tss.banking.dto.request.LoginRequestDTO;
import com.tss.banking.dto.request.PasswordChangeRequestDTO;
import com.tss.banking.dto.response.CustomerResponseDTO;
import com.tss.banking.dto.response.PagedResponseDTO;
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.eums.CustomerStatus;
import com.tss.banking.exception.CustomerNotFoundException;
import com.tss.banking.exception.DuplicateResourceException;
import com.tss.banking.exception.ValidationException;
import com.tss.banking.repository.CustomerRepository;
import com.tss.banking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponseDTO registerCustomer(CustomerRegistrationRequestDTO request) {
        log.info("Registering new customer with email: {}", request.getEmail());
        
        // Check for duplicate email
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Customer with email " + request.getEmail() + " already exists");
        }
        
        // Check for duplicate phone number
        if (request.getPhone() != null && customerRepository.existsByPhoneNumber(request.getPhone())) {
            throw new DuplicateResourceException("Customer with phone number " + request.getPhone() + " already exists");
        }
        
        // Parse full name into first and last name
        String[] nameParts = request.getFullName().trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(request.getEmail())
                .phoneNumber(request.getPhone())
                .address(request.getAddress())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(CustomerStatus.ACTIVE)
                .emailVerified(false)
                .phoneVerified(false)
                .registrationDate(LocalDateTime.now())
                .lastUpdated(LocalDateTime.now())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer registered successfully with ID: {}", savedCustomer.getId());
        return mapToResponseDTO(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long customerId) {
        log.debug("Fetching customer with ID: {}", customerId);
        Customer customer = findCustomerById(customerId);
        return mapToResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long customerId, CustomerUpdateRequestDTO request) {
        log.info("Updating customer with ID: {}", customerId);
        
        Customer customer = findCustomerById(customerId);
        
        // Check for duplicate email (excluding current customer)
        if (request.getEmail() != null && !request.getEmail().equals(customer.getEmail()) &&
            customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Customer with email " + request.getEmail() + " already exists");
        }
        
        // Check for duplicate phone number (excluding current customer)
        if (request.getPhone() != null && !request.getPhone().equals(customer.getPhoneNumber()) &&
            customerRepository.existsByPhoneNumber(request.getPhone())) {
            throw new DuplicateResourceException("Customer with phone number " + request.getPhone() + " already exists");
        }
        
        // Update fields if provided
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            String[] nameParts = request.getFullName().trim().split("\\s+", 2);
            customer.setFirstName(nameParts[0]);
            customer.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        }
        
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
            customer.setEmailVerified(false); // Reset verification on email change
        }
        
        if (request.getPhone() != null) {
            customer.setPhoneNumber(request.getPhone());
            customer.setPhoneVerified(false); // Reset verification on phone change
        }
        
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
        
        customer.setLastUpdated(LocalDateTime.now());
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with ID: {}", customerId);
        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    public CustomerResponseDTO deactivateCustomer(Long customerId) {
        log.info("Deactivating customer with ID: {}", customerId);
        Customer customer = findCustomerById(customerId);
        customer.setStatus(CustomerStatus.INACTIVE);
        customer.setLastUpdated(LocalDateTime.now());
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer deactivated successfully with ID: {}", customerId);
        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    public CustomerResponseDTO activateCustomer(Long customerId) {
        log.info("Activating customer with ID: {}", customerId);
        Customer customer = findCustomerById(customerId);
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setLastUpdated(LocalDateTime.now());
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer activated successfully with ID: {}", customerId);
        return mapToResponseDTO(updatedCustomer);
    }

    @Transactional(readOnly = true)
    public boolean validateCustomerLogin(LoginRequestDTO request) {
        log.debug("Validating login for email: {}", request.getEmail());
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (customer == null) {
            return false;
        }

        return passwordEncoder.matches(request.getPassword(), customer.getPasswordHash()) &&
               customer.getStatus() == CustomerStatus.ACTIVE;
    }

    public void changePassword(Long customerId, String currentPassword, String newPassword) {
        log.info("Changing password for customer ID: {}", customerId);
        Customer customer = findCustomerById(customerId);

        if (!passwordEncoder.matches(currentPassword, customer.getPasswordHash())) {
            throw new ValidationException("Current password is incorrect");
        }

        customer.setPasswordHash(passwordEncoder.encode(newPassword));
        customer.setLastUpdated(LocalDateTime.now());
        customerRepository.save(customer);
        log.info("Password changed successfully for customer ID: {}", customerId);
    }

    public void verifyEmail(Long customerId) {
        log.info("Verifying email for customer ID: {}", customerId);
        Customer customer = findCustomerById(customerId);
        customer.setEmailVerified(true);
        customer.setLastUpdated(LocalDateTime.now());
        customerRepository.save(customer);
        log.info("Email verified successfully for customer ID: {}", customerId);
    }

    public void verifyPhone(Long customerId) {
        log.info("Verifying phone for customer ID: {}", customerId);
        Customer customer = findCustomerById(customerId);
        customer.setPhoneVerified(true);
        customer.setLastUpdated(LocalDateTime.now());
        customerRepository.save(customer);
        log.info("Phone verified successfully for customer ID: {}", customerId);
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDirection) {
        log.debug("Fetching customers - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, sortDirection);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        
        List<CustomerResponseDTO> customers = customerPage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return PagedResponseDTO.<CustomerResponseDTO>builder()
                .content(customers)
                .page(customerPage.getNumber())
                .size(customerPage.getSize())
                .totalElements(customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .first(customerPage.isFirst())
                .last(customerPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<CustomerResponseDTO> searchCustomers(String searchTerm, int page, int size) {
        log.debug("Searching customers with term: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findBySearchTerm(searchTerm, pageable);
        
        List<CustomerResponseDTO> customers = customerPage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return PagedResponseDTO.<CustomerResponseDTO>builder()
                .content(customers)
                .page(customerPage.getNumber())
                .size(customerPage.getSize())
                .totalElements(customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .first(customerPage.isFirst())
                .last(customerPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getCustomersByStatus(CustomerStatus status) {
        log.debug("Fetching customers with status: {}", status);
        List<Customer> customers = customerRepository.findByStatus(status);
        return customers.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getCustomerCount() {
        return customerRepository.count();
    }

    @Transactional(readOnly = true)
    public long getActiveCustomerCount() {
        return customerRepository.countByStatus(CustomerStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return customerRepository.existsByPhoneNumber(phoneNumber);
    }

    // Helper methods
    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .dateOfBirth(customer.getDateOfBirth())
                .status(customer.getStatus())
                .emailVerified(customer.isEmailVerified())
                .phoneVerified(customer.isPhoneVerified())
                .registrationDate(customer.getRegistrationDate())
                .lastUpdated(customer.getLastUpdated())
                .build();
    }

	@Override
	@Transactional(readOnly = true)
	public CustomerResponseDTO getCustomerByEmail(String email) {
		log.debug("Fetching customer with email: {}", email);
		Customer customer = customerRepository.findByEmail(email)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
		return mapToResponseDTO(customer);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
		log.debug("Fetching all customers with pagination");
		Page<Customer> customerPage = customerRepository.findAll(pageable);
		return customerPage.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomerResponseDTO> getCustomersByStatus(CustomerStatus status, Pageable pageable) {
		log.debug("Fetching customers with status: {}", status);
		Page<Customer> customerPage = customerRepository.findByStatus(status, pageable);
		return customerPage.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomerResponseDTO> searchCustomers(String searchTerm, Pageable pageable) {
		log.debug("Searching customers with term: {}", searchTerm);
		Page<Customer> customerPage = customerRepository.findBySearchTerm(searchTerm, pageable);
		return customerPage.map(this::mapToResponseDTO);
	}

	@Override
	public void changePassword(Long customerId, PasswordChangeRequestDTO passwordChangeRequest) {
		log.info("Changing password for customer ID: {}", customerId);
		Customer customer = findCustomerById(customerId);

		if (!passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), customer.getPasswordHash())) {
			throw new ValidationException("Current password is incorrect");
		}

		customer.setPasswordHash(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
		customer.setLastUpdated(LocalDateTime.now());
		customerRepository.save(customer);
		log.info("Password changed successfully for customer ID: {}", customerId);
	}

	@Override
	public CustomerResponseDTO updateCustomerStatus(Long customerId, CustomerStatus status) {
		log.info("Updating customer status to {} for customer ID: {}", status, customerId);
		Customer customer = findCustomerById(customerId);
		customer.setStatus(status);
		customer.setLastUpdated(LocalDateTime.now());
		Customer updatedCustomer = customerRepository.save(customer);
		log.info("Customer status updated successfully for ID: {}", customerId);
		return mapToResponseDTO(updatedCustomer);
	}

	@Override
	public CustomerResponseDTO suspendCustomer(Long customerId) {
		log.info("Suspending customer with ID: {}", customerId);
		Customer customer = findCustomerById(customerId);
		customer.setStatus(CustomerStatus.SUSPENDED);
		customer.setLastUpdated(LocalDateTime.now());
		Customer updatedCustomer = customerRepository.save(customer);
		log.info("Customer suspended successfully with ID: {}", customerId);
		return mapToResponseDTO(updatedCustomer);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isCustomerActive(Long customerId) {
		log.debug("Checking if customer is active for ID: {}", customerId);
		Customer customer = findCustomerById(customerId);
		return customer.getStatus() == CustomerStatus.ACTIVE;
	}

	@Override
	@Transactional(readOnly = true)
	public Customer getCustomerEntityById(Long customerId) {
		log.debug("Fetching customer entity with ID: {}", customerId);
		return findCustomerById(customerId);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> getCustomerEntityByEmail(String email) {
		log.debug("Fetching customer entity with email: {}", email);
		return customerRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> validateCustomerCredentials(String email, String password) {
		log.debug("Validating customer credentials for email: {}", email);
		Optional<Customer> customerOpt = customerRepository.findByEmail(email);
		
		if (customerOpt.isPresent()) {
			Customer customer = customerOpt.get();
			if (passwordEncoder.matches(password, customer.getPasswordHash()) && 
				customer.getStatus() == CustomerStatus.ACTIVE) {
				return customerOpt;
			}
		}
		return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public int getCustomerAccountCount(Long customerId) {
		log.debug("Getting account count for customer ID: {}", customerId);
		// This would require AccountRepository injection to count accounts by customer
		// For now, returning 0 as placeholder - needs AccountRepository dependency
		return 0;
	}

	@Override
	public void deleteCustomer(Long customerId) {
		log.info("Deleting customer with ID: {}", customerId);
		Customer customer = findCustomerById(customerId);
		
		// Soft delete by setting status to DELETED instead of hard delete
		customer.setStatus(CustomerStatus.INACTIVE);
		customer.setLastUpdated(LocalDateTime.now());
		customerRepository.save(customer);
		
		// For hard delete, use: customerRepository.delete(customer);
		log.info("Customer deleted successfully with ID: {}", customerId);
	}
}
