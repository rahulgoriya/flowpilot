package flowpilot.dto;

import flowpilot.entity.QuoteStatus;
import java.time.LocalDateTime;

public class QuoteResponse {
    
    private Long quoteId;
    private Long enquiryId;
    private Integer subtotal;
    private Integer addOns;
    private Integer totalAmount;
    private QuoteStatus status;
    private LocalDateTime expiresAt;
    private String breakdown; // e.g., "Base 2BHK: 2500 + Deep Clean: 1000"

    public QuoteResponse(Long quoteId, Long enquiryId, Integer subtotal, Integer addOns, Integer totalAmount, QuoteStatus status, LocalDateTime expiresAt, String breakdown) {
        this.quoteId = quoteId;
        this.enquiryId = enquiryId;
        this.subtotal = subtotal;
        this.addOns = addOns;
        this.totalAmount = totalAmount;
        this.status = status;
        this.expiresAt = expiresAt;
        this.breakdown = breakdown;
    }

    public Long getQuoteId() { return quoteId; }
    public Long getEnquiryId() { return enquiryId; }
    public Integer getSubtotal() { return subtotal; }
    public Integer getAddOns() { return addOns; }
    public Integer getTotalAmount() { return totalAmount; }
    public QuoteStatus getStatus() { return status; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public String getBreakdown() { return breakdown; }
}