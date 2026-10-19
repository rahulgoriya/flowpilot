package flowpilot.dto;

import flowpilot.entity.EnquiryStatus;
import java.time.LocalDateTime;

public class EnquiryResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private String rawMessage;
    private EnquiryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // AI Extracted fields
    private String extractedServiceType;
    private String extractedPropertyType;
    private Integer extractedBedrooms;
    private String extractedLocation;
    private String extractedPreferredDateTime;
    private String extractedIntent;

    public EnquiryResponse() {}

    public EnquiryResponse(Long id, Long customerId, String customerName, String rawMessage, EnquiryStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, String extractedServiceType, String extractedPropertyType, Integer extractedBedrooms, String extractedLocation, String extractedPreferredDateTime, String extractedIntent) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.rawMessage = rawMessage;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.extractedServiceType = extractedServiceType;
        this.extractedPropertyType = extractedPropertyType;
        this.extractedBedrooms = extractedBedrooms;
        this.extractedLocation = extractedLocation;
        this.extractedPreferredDateTime = extractedPreferredDateTime;
        this.extractedIntent = extractedIntent;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getRawMessage() { return rawMessage; }
    public void setRawMessage(String rawMessage) { this.rawMessage = rawMessage; }

    public EnquiryStatus getStatus() { return status; }
    public void setStatus(EnquiryStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getExtractedServiceType() { return extractedServiceType; }
    public void setExtractedServiceType(String extractedServiceType) { this.extractedServiceType = extractedServiceType; }

    public String getExtractedPropertyType() { return extractedPropertyType; }
    public void setExtractedPropertyType(String extractedPropertyType) { this.extractedPropertyType = extractedPropertyType; }

    public Integer getExtractedBedrooms() { return extractedBedrooms; }
    public void setExtractedBedrooms(Integer extractedBedrooms) { this.extractedBedrooms = extractedBedrooms; }

    public String getExtractedLocation() { return extractedLocation; }
    public void setExtractedLocation(String extractedLocation) { this.extractedLocation = extractedLocation; }

    public String getExtractedPreferredDateTime() { return extractedPreferredDateTime; }
    public void setExtractedPreferredDateTime(String extractedPreferredDateTime) { this.extractedPreferredDateTime = extractedPreferredDateTime; }

    public String getExtractedIntent() { return extractedIntent; }
    public void setExtractedIntent(String extractedIntent) { this.extractedIntent = extractedIntent; }
}