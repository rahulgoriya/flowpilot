package flowpilot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enquiries")
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "raw_message", nullable = false, length = 2000)
    private String rawMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnquiryStatus status;

    @Column(name = "extracted_service_type", length = 50)
    private String extractedServiceType;

    @Column(name = "extracted_property_type", length = 50)
    private String extractedPropertyType;

    @Column(name = "extracted_bedrooms")
    private Integer extractedBedrooms;

    @Column(name = "extracted_location", length = 100)
    private String extractedLocation;

    @Column(name = "extracted_preferred_date_time", length = 100)
    private String extractedPreferredDateTime;

    @Column(name = "extracted_intent", length = 50)
    private String extractedIntent;

    @Column(name = "ai_raw_response", columnDefinition = "TEXT")
    private String aiRawResponse;

    // NEW: Relationship with Quote
    @OneToMany(mappedBy = "enquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quote> quotes = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Enquiry() {}

    public Enquiry(Customer customer, String rawMessage) {
        this.customer = customer;
        this.rawMessage = rawMessage;
        this.status = EnquiryStatus.NEW;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = EnquiryStatus.NEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

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

    public String getAiRawResponse() { return aiRawResponse; }
    public void setAiRawResponse(String aiRawResponse) { this.aiRawResponse = aiRawResponse; }

    public List<Quote> getQuotes() { return quotes; }
    public void setQuotes(List<Quote> quotes) { this.quotes = quotes; }
}