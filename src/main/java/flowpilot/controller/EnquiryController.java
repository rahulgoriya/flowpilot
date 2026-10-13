package flowpilot.controller;

import flowpilot.dto.EnquiryRequest;
import flowpilot.dto.EnquiryResponse;
import flowpilot.dto.EnquirySummaryResponse;
import flowpilot.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping
    public ResponseEntity<EnquiryResponse> create(@Valid @RequestBody EnquiryRequest request) {
        EnquiryResponse saved = enquiryService.createEnquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<EnquirySummaryResponse> getAll() {
        return enquiryService.getAllEnquiries();
    }

    @GetMapping("/{id}")
    public EnquiryResponse getById(@PathVariable Long id) {
        return enquiryService.getEnquiryById(id);
    }

    @GetMapping("/by-customer/{customerId}")
    public List<EnquirySummaryResponse> getByCustomer(@PathVariable Long customerId) {
        return enquiryService.getEnquiriesByCustomer(customerId);
    }
}