package flowpilot.dto;

public class QuoteResponse {
    
    private Long enquiryId;
    private Integer totalPrice;
    private String breakdown;

    public QuoteResponse(Long enquiryId, Integer totalPrice, String breakdown) {
        this.enquiryId = enquiryId;
        this.totalPrice = totalPrice;
        this.breakdown = breakdown;
    }

    public Long getEnquiryId() { return enquiryId; }
    public Integer getTotalPrice() { return totalPrice; }
    public String getBreakdown() { return breakdown; }
}