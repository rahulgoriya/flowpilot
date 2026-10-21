package flowpilot.service;

import flowpilot.dto.QuoteResponse;
import flowpilot.entity.Enquiry;
import flowpilot.entity.Quote;
import flowpilot.entity.QuoteStatus;
import flowpilot.exception.QuoteNotFoundException;
import flowpilot.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public QuoteResponse getQuoteById(Long id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));
        return toResponse(quote);
    }

    public QuoteResponse acceptQuote(Long id) {
        Quote quote = getValidPendingQuote(id);
        quote.setStatus(QuoteStatus.ACCEPTED);
        Quote saved = quoteRepository.save(quote);
        return toResponse(saved);
    }

    public QuoteResponse rejectQuote(Long id) {
        Quote quote = getValidPendingQuote(id);
        quote.setStatus(QuoteStatus.REJECTED);
        Quote saved = quoteRepository.save(quote);
        return toResponse(saved);
    }

    // Helper method to ensure the quote exists, is PENDING, and is not expired
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

    // Helper to map Entity to DTO
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