package flowpilot.service;

import flowpilot.dto.BookingResponse;
import flowpilot.entity.Booking;
import flowpilot.entity.Quote;
import flowpilot.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Creates a booking from an accepted quote.
     * For now, we set the scheduled date to 2 days from now as a placeholder.
     */
    public Booking createBookingFromQuote(Quote quote) {
        // In a real app, we'd parse quote.getEnquiry().getExtractedPreferredDateTime()
        // For now, let's just schedule it 2 days from today at 10:00 AM
        LocalDateTime scheduledTime = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
        
        Booking booking = new Booking(quote, quote.getEnquiry().getCustomer(), scheduledTime);
        return bookingRepository.save(booking);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getQuote().getId(),
                booking.getCustomer().getId(),
                booking.getCustomer().getName(),
                booking.getScheduledDateTime(),
                booking.getStatus()
        );
    }
}