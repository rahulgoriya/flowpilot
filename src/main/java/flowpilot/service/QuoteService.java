package flowpilot.service;

import flowpilot.dto.QuoteResponse;
import flowpilot.entity.Enquiry;
import flowpilot.entity.EnquiryStatus;
import flowpilot.entity.Quote;
import flowpilot.entity.QuoteStatus;
import flowpilot.exception.QuoteNotFoundException;
import flowpilot.repository.EnquiryRepository;
import flowpilot.repository.QuoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final EnquiryRepository enquiryRepository; // NEW: Needed for Orchestration

    public QuoteService(QuoteRepository quoteRepository, EnquiryRepository enquiryRepository) {
        this.quoteRepository = quoteRepository;
        this.enquiryRepository = enquiryRepository;
    }

    public QuoteResponse getQuoteById(Long id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));
        return toResponse(quote);
    }

    /**
     * ORCHESTRATOR: Accepting a quote updates the Quote AND the Enquiry.
     * The @Transactional ensures that if one fails, both roll back.
     */
    @Transactional
    public QuoteResponse acceptQuote(Long id) {
        Quote quote = getValidPendingQuote(id);
        
        // 1. Update Quote Status
        quote.setStatus(QuoteStatus.ACCEPTED);
        Quote savedQuote = quoteRepository.save(quote);

        // 2. Update the Parent Enquiry Status
        Enquiry enquiry = savedQuote.getEnquiry();
        enquiry.setStatus(EnquiryStatus.ACCEPTED);
        enquiryRepository.save(enquiry);

        return toResponse(savedQuote);
    }

    @Transactional
    public QuoteResponse rejectQuote(Long id) {
        Quote quote = getValidPendingQuote(id);
        
        // 1. Update Quote Status
        quote.setStatus(QuoteStatus.REJECTED);
        Quote savedQuote = quoteRepository.save(quote);

        // 2. Update the Parent Enquiry Status
        Enquiry enquiry = savedQuote.getEnquiry();
        enquiry.setStatus(EnquiryStatus.REJECTED);
        enquiryRepository.save(enquiry);

        return toResponse(savedQuote);
    }

    private Quote getValidPendingQuote(Long id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));

        if (quote.getStatus() != QuoteStatus.PENDING) {
            throw new RuntimeException("Cannot modify quote. Current status is: " + quote.getStatus());
        }

        if (quote.getExpiresAt().isBefore(LocalDateTime.now())) {
            quote.setStatus(QuoteStatus.EXPIRED);
            quoteRepository.save(quote);
            throw new RuntimeException("Quote has expired.");
        }

        return quote;
    }

    private QuoteResponse toResponse(Quote quote) {
        Enquiry enquiry = quote.getEnquiry();
        return new QuoteResponse(
                quote.getId(),
                enquiry.getId(),
                quote.getSubtotal(),
                quote.getAddOns(),
                quote.getTotalAmount(),
                quote.getStatus(),
                quote.getExpiresAt(),
                "Base: ₹" + quote.getSubtotal() + " + Add-on: ₹" + quote.getAddOns()
        );
    }
}