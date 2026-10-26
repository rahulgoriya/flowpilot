package flowpilot.dto;

import flowpilot.entity.EnquiryStatus;
import java.time.LocalDateTime;

public class EnquirySummaryResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String messagePreview;
    private EnquiryStatus status;
    private LocalDateTime createdAt;
    
    // NAYE FIELDS: Taaki UI decide kar sake ki quote button dikhana hai ya nahi
    private String extractedServiceType;
    private String extractedPropertyType;

    public EnquirySummaryResponse(Long id, Long customerId, String customerName, String messagePreview, EnquiryStatus status, LocalDateTime createdAt, String extractedServiceType, String extractedPropertyType) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.messagePreview = messagePreview;
        this.status = status;
        this.createdAt = createdAt;
        this.extractedServiceType = extractedServiceType;
        this.extractedPropertyType = extractedPropertyType;
    }

    // Getters (Add them)
    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getMessagePreview() { return messagePreview; }
    public EnquiryStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getExtractedServiceType() { return extractedServiceType; }
    public String getExtractedPropertyType() { return extractedPropertyType; }
}