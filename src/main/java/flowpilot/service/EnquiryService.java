package flowpilot.service;

import flowpilot.dto.EnquiryRequest;
import flowpilot.dto.EnquiryResponse;
import flowpilot.entity.Customer;
import flowpilot.entity.Enquiry;
import flowpilot.exception.CustomerNotFoundException;
import flowpilot.exception.EnquiryNotFoundException;
import flowpilot.repository.CustomerRepository;
import flowpilot.repository.EnquiryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final CustomerRepository customerRepository;

    public EnquiryService(EnquiryRepository enquiryRepository,
                          CustomerRepository customerRepository) {
        this.enquiryRepository = enquiryRepository;
        this.customerRepository = customerRepository;
    }

    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        Enquiry enquiry = new Enquiry(customer, request.getRawMessage());
        Enquiry saved = enquiryRepository.save(enquiry);
        return toResponse(saved);
    }

    public List<EnquiryResponse> getAllEnquiries() {
        return enquiryRepository.findAllWithCustomerOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EnquiryResponse getEnquiryById(Long id) {
        Enquiry enquiry = enquiryRepository.findByIdWithCustomer(id)
                .orElseThrow(() -> new EnquiryNotFoundException(id));
        return toResponse(enquiry);
    }

    public List<EnquiryResponse> getEnquiriesByCustomer(Long customerId) {
        // Verify customer exists first — gives a friendly 404 if not
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        return enquiryRepository.findByCustomerIdWithCustomer(customerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---- Mapping helper ----

    private EnquiryResponse toResponse(Enquiry enquiry) {
        return new EnquiryResponse(
                enquiry.getId(),
                enquiry.getCustomer().getId(),
                enquiry.getCustomer().getName(),
                enquiry.getRawMessage(),
                enquiry.getStatus(),
                enquiry.getCreatedAt(),
                enquiry.getUpdatedAt()
        );
    }
}