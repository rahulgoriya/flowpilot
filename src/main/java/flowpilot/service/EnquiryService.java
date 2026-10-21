package flowpilot.service;

import flowpilot.dto.AIExtractedData;
import flowpilot.dto.EnquiryRequest;
import flowpilot.dto.EnquiryResponse;
import flowpilot.dto.EnquirySummaryResponse;
import flowpilot.entity.Customer;
import flowpilot.entity.Enquiry;
import flowpilot.entity.EnquiryStatus;
import flowpilot.exception.CustomerNotFoundException;
import flowpilot.exception.EnquiryNotFoundException;
import flowpilot.repository.CustomerRepository;
import flowpilot.repository.EnquiryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnquiryService {

    private static final Logger log = LoggerFactory.getLogger(EnquiryService.class);

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

    /**
     * ORCHESTRATOR: Reads like a story.
     */
    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        // 1. Fetch Customer (Verify existence)
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        // 2. Create Entity
        Enquiry enquiry = new Enquiry(customer, request.getRawMessage());

        // 3. Delegate AI Processing
        processAIExtraction(enquiry);

        // 4. Save and Return
        Enquiry saved = enquiryRepository.save(enquiry);
        return toResponse(saved);
    }

    /**
     * HELPER: Handles the messy AI logic and fallback.
     * By separating this, createEnquiry() remains clean.
     */
    private void processAIExtraction(Enquiry enquiry) {
        try {
            AIExtractedData extractedData = geminiService.extractEnquiryData(enquiry.getRawMessage());

            // Map AI data to Entity (with normalization)
            enquiry.setExtractedServiceType(normalize(extractedData.getServiceType()));
            enquiry.setExtractedPropertyType(normalize(extractedData.getPropertyType()));
            enquiry.setExtractedBedrooms(extractedData.getBedrooms());
            enquiry.setExtractedLocation(normalize(extractedData.getLocation()));
            enquiry.setExtractedPreferredDateTime(normalize(extractedData.getPreferredDateTime()));
            enquiry.setExtractedIntent(normalize(extractedData.getIntent()));
            
            enquiry.setAiRawResponse(extractedData.getRawJsonResponse());
            enquiry.setStatus(EnquiryStatus.EXTRACTED);

        } catch (Exception e) {
            // Fallback Safety Net
            log.warn("AI failed for message: {}. Saving as AI_FAILED.", enquiry.getRawMessage());
            enquiry.setStatus(EnquiryStatus.AI_FAILED);
            enquiry.setAiRawResponse("{\"error\": \"" + e.getMessage() + "\"}");
        }
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

    // Helper: Normalizes empty AI strings to null
    private String normalize(String value) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
            return null;
        }
        return value;
    }

    // Helper: Entity to DTO Mapping
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
                enquiry.getExtractedIntent(),
                enquiry.getAiRawResponse()
        );
    }

    // Helper: Entity to Summary DTO
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