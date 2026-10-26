package flowpilot.controller;

import flowpilot.dto.QuoteResponse;
import flowpilot.service.QuoteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@CrossOrigin(origins = "http://localhost:5173") // NEW: Allows React frontend to connect
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    // GET a specific quote
    @GetMapping("/{id}")
    public QuoteResponse getQuote(@PathVariable Long id) {
        return quoteService.getQuoteById(id);
    }

    // PATCH to accept a quote
    @PatchMapping("/{id}/accept")
    public QuoteResponse acceptQuote(@PathVariable Long id) {
        return quoteService.acceptQuote(id);
    }

    // PATCH to reject a quote
    @PatchMapping("/{id}/reject")
    public QuoteResponse rejectQuote(@PathVariable Long id) {
        return quoteService.rejectQuote(id);
    }
}