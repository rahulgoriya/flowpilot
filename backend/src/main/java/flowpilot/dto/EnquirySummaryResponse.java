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

    public EnquirySummaryResponse() {
    }

    public EnquirySummaryResponse(Long id, Long customerId, String customerName,
                                  String messagePreview, EnquiryStatus status,
                                  LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.messagePreview = messagePreview;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMessagePreview() { return messagePreview; }
    public void setMessagePreview(String messagePreview) { this.messagePreview = messagePreview; }

    public EnquiryStatus getStatus() { return status; }
    public void setStatus(EnquiryStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}