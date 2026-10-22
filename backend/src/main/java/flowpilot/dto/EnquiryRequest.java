package flowpilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EnquiryRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Message is required")
    @Size(min = 5, max = 2000, message = "Message must be between 5 and 2000 characters")
    private String rawMessage;

    public EnquiryRequest() {
    }

    public EnquiryRequest(Long customerId, String rawMessage) {
        this.customerId = customerId;
        this.rawMessage = rawMessage;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getRawMessage() { return rawMessage; }
    public void setRawMessage(String rawMessage) { this.rawMessage = rawMessage; }
}