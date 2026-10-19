package flowpilot.service;

import flowpilot.dto.AIExtractedData;
import flowpilot.dto.EnquiryRequest;
import flowpilot.dto.EnquiryResponse;
import flowpilot.dto.EnquirySummaryResponse;
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
    private final GeminiService geminiService;

    public EnquiryService(EnquiryRepository enquiryRepository,
                          CustomerRepository customerRepository,
                          GeminiService geminiService) {
        this.enquiryRepository = enquiryRepository;
        this.customerRepository = customerRepository;
        this.geminiService = geminiService;
    }

    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        Enquiry enquiry = new Enquiry(customer, request.getRawMessage());

        // Call AI and extract data
        AIExtractedData extractedData = geminiService.extractEnquiryData(request.getRawMessage());

        // Save extracted data into the enquiry
        enquiry.setExtractedServiceType(extractedData.getServiceType());
        enquiry.setExtractedPropertyType(extractedData.getPropertyType());
        enquiry.setExtractedBedrooms(extractedData.getBedrooms());
        enquiry.setExtractedLocation(extractedData.getLocation());
        enquiry.setExtractedPreferredDateTime(extractedData.getPreferredDateTime());
        enquiry.setExtractedIntent(extractedData.getIntent());

        Enquiry saved = enquiryRepository.save(enquiry);
        return toResponse(saved);
    }

    public List<EnquirySummaryResponse> getAllEnquiries() {
        return enquiryRepository.findAllWithCustomerOrderByCreatedAtDesc()
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    public EnquiryResponse getEnquiryById(Long id) {
        Enquiry enquiry = enquiryRepository.findByIdWithCustomer(id)
                .orElseThrow(() -> new EnquiryNotFoundException(id));
        return toResponse(enquiry);
    }

    public List<EnquirySummaryResponse> getEnquiriesByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        return enquiryRepository.findByCustomerIdWithCustomer(customerId)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    // ---- Detail mapping ----
    private EnquiryResponse toResponse(Enquiry enquiry) {
        return new EnquiryResponse(
                enquiry.getId(),
                enquiry.getCustomer().getId(),
                enquiry.getCustomer().getName(),
                enquiry.getRawMessage(),
                enquiry.getStatus(),
                enquiry.getCreatedAt(),
                enquiry.getUpdatedAt(),
                enquiry.getExtractedServiceType(),
                enquiry.getExtractedPropertyType(),
                enquiry.getExtractedBedrooms(),
                enquiry.getExtractedLocation(),
                enquiry.getExtractedPreferredDateTime(),
                enquiry.getExtractedIntent()
        );
    }

    // ---- Summary mapping ----
    private EnquirySummaryResponse toSummary(Enquiry enquiry) {
        return new EnquirySummaryResponse(
                enquiry.getId(),
                enquiry.getCustomer().getId(),
                enquiry.getCustomer().getName(),
                truncateMessage(enquiry.getRawMessage()),
                enquiry.getStatus(),
                enquiry.getCreatedAt()
        );
    }

    private String truncateMessage(String message) {
        if (message == null) return null;
        if (message.length() <= 80) return message;
        return message.substring(0, 80) + "...";
    }
}