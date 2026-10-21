package flowpilot.service;

import flowpilot.dto.AIExtractedData;
import flowpilot.dto.EnquiryRequest;
import flowpilot.dto.EnquiryResponse;
import flowpilot.dto.EnquirySummaryResponse;
import flowpilot.dto.PriceCalculation;
import flowpilot.dto.QuoteResponse;
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
    private final PricingService pricingService; // NEW: Injected Pricing Service

    public EnquiryService(EnquiryRepository enquiryRepository,
                          CustomerRepository customerRepository,
                          GeminiService geminiService,
                          PricingService pricingService) { // NEW: Added to constructor
        this.enquiryRepository = enquiryRepository;
        this.customerRepository = customerRepository;
        this.geminiService = geminiService;
        this.pricingService = pricingService;
    }

    /**
     * ORCHESTRATOR: Reads like a story.
     */
    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        Enquiry enquiry = new Enquiry(customer, request.getRawMessage());

        processAIExtraction(enquiry);

        Enquiry saved = enquiryRepository.save(enquiry);
        return toResponse(saved);
    }

    /**
     * NEW: Generates a Quote for a specific Enquiry using deterministic pricing.
     */
    public QuoteResponse generateQuote(Long enquiryId) {
        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new EnquiryNotFoundException(enquiryId));

        // Safety check: Only allow quotes for fully extracted enquiries
        if (enquiry.getStatus() != EnquiryStatus.EXTRACTED) {
            throw new RuntimeException("Cannot generate quote. Enquiry status is: " + enquiry.getStatus());
        }

        PriceCalculation priceCalc = pricingService.calculatePrice(
                enquiry.getExtractedPropertyType(), 
                enquiry.getExtractedServiceType()
        );

        return new QuoteResponse(enquiry.getId(), priceCalc.getTotalPrice(), priceCalc.getBreakdown());
    }

    private void processAIExtraction(Enquiry enquiry) {
        try {
            AIExtractedData extractedData = geminiService.extractEnquiryData(enquiry.getRawMessage());

            enquiry.setExtractedServiceType(normalize(extractedData.getServiceType()));
            enquiry.setExtractedPropertyType(normalize(extractedData.getPropertyType()));
            enquiry.setExtractedBedrooms(extractedData.getBedrooms());
            enquiry.setExtractedLocation(normalize(extractedData.getLocation()));
            enquiry.setExtractedPreferredDateTime(normalize(extractedData.getPreferredDateTime()));
            enquiry.setExtractedIntent(normalize(extractedData.getIntent()));
            
            enquiry.setAiRawResponse(extractedData.getRawJsonResponse());
            enquiry.setStatus(EnquiryStatus.EXTRACTED);

        } catch (Exception e) {
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

    private String normalize(String value) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
            return null;
        }
        return value;
    }

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