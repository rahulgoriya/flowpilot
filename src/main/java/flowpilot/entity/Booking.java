package flowpilot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The Quote that was accepted for this booking
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id", nullable = false, unique = true)
    private Quote quote;

    // The Customer receiving the service
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "scheduled_date_time", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Booking() {}

    public Booking(Quote quote, Customer customer, LocalDateTime scheduledDateTime) {
        this.quote = quote;
        this.customer = customer;
        this.scheduledDateTime = scheduledDateTime;
        this.status = BookingStatus.SCHEDULED;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Quote getQuote() { return quote; }
    public void setQuote(Quote quote) { this.quote = quote; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public LocalDateTime getScheduledDateTime() { return scheduledDateTime; }
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) { this.scheduledDateTime = scheduledDateTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}