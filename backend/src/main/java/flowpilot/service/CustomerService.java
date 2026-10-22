package flowpilot.service;

import flowpilot.dto.CustomerRequest;
import flowpilot.dto.CustomerResponse;
import flowpilot.entity.Customer;
import flowpilot.exception.CustomerNotFoundException;
import flowpilot.exception.DuplicateEmailException;
import flowpilot.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        // Check for duplicate email BEFORE insert
        customerRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    throw new DuplicateEmailException(request.getEmail());
                });

        Customer customer = toEntity(request);
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return toResponse(customer);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // If email is being changed, check for duplicate
        if (!existing.getEmail().equals(request.getEmail())) {
            customerRepository.findByEmail(request.getEmail())
                    .ifPresent(other -> {
                        throw new DuplicateEmailException(request.getEmail());
                    });
        }

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());

        Customer updated = customerRepository.save(existing);
        return toResponse(updated);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customerRepository.delete(customer);
    }

    // ---- Mapping helpers ----

    private Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        return customer;
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt()
        );
    }
}