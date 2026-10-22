package flowpilot.dto;

import flowpilot.entity.BookingStatus;
import java.time.LocalDateTime;

public class BookingResponse {
    
    private Long bookingId;
    private Long quoteId;
    private Long customerId;
    private String customerName;
    private LocalDateTime scheduledDateTime;
    private BookingStatus status;

    public BookingResponse(Long bookingId, Long quoteId, Long customerId, String customerName, LocalDateTime scheduledDateTime, BookingStatus status) {
        this.bookingId = bookingId;
        this.quoteId = quoteId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.scheduledDateTime = scheduledDateTime;
        this.status = status;
    }

    public Long getBookingId() { return bookingId; }
    public Long getQuoteId() { return quoteId; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getScheduledDateTime() { return scheduledDateTime; }
    public BookingStatus getStatus() { return status; }
}