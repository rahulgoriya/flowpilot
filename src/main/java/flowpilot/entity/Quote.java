package flowpilot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many quotes can belong to one Enquiry
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enquiry_id", nullable = false)
    private Enquiry enquiry;

    @Column(name = "subtotal", nullable = false)
    private Integer subtotal;

    @Column(name = "add_ons", nullable = false)
    private Integer addOns;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuoteStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // NEW: One-to-One relationship with Booking
    @OneToOne(mappedBy = "quote", cascade = CascadeType.ALL)
    private Booking booking;

    public Quote() {}

    public Quote(Enquiry enquiry, Integer subtotal, Integer addOns, Integer totalAmount) {
        this.enquiry = enquiry;
        this.subtotal = subtotal;
        this.addOns = addOns;
        this.totalAmount = totalAmount;
        this.status = QuoteStatus.PENDING;
        this.expiresAt = LocalDateTime.now().plusDays(7); // Valid for 7 days
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

    public Enquiry getEnquiry() { return enquiry; }
    public void setEnquiry(Enquiry enquiry) { this.enquiry = enquiry; }

    public Integer getSubtotal() { return subtotal; }
    public void setSubtotal(Integer subtotal) { this.subtotal = subtotal; }

    public Integer getAddOns() { return addOns; }
    public void setAddOns(Integer addOns) { this.addOns = addOns; }

    public Integer getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Integer totalAmount) { this.totalAmount = totalAmount; }

    public QuoteStatus getStatus() { return status; }
    public void setStatus(QuoteStatus status) { this.status = status; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // NEW: Getter and Setter for Booking
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}