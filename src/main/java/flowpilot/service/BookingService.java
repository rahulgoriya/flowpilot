package flowpilot.service;

import flowpilot.dto.BookingResponse;
import flowpilot.entity.Booking;
import flowpilot.entity.BookingStatus;
import flowpilot.entity.Quote;
import flowpilot.exception.BookingConflictException;
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

    public Booking createBookingFromQuote(Quote quote) {
        // 1. Calculate the scheduled time (2 days from now at 10:00 AM)
        // Note: We zero out seconds and nanoseconds for accurate comparison
        LocalDateTime scheduledTime = LocalDateTime.now().plusDays(2)
                .withHour(10).withMinute(0).withSecond(0).withNano(0);

        // 2. Check for conflicts
        List<Booking> existingBookings = bookingRepository
                .findByScheduledDateTimeAndStatus(scheduledTime, BookingStatus.SCHEDULED);

        if (!existingBookings.isEmpty()) {
            throw new BookingConflictException(
                "The time slot " + scheduledTime + " is already booked. Please choose a different time."
            );
        }

        // 3. No conflict, save the booking
        Booking booking = new Booking(quote, quote.getEnquiry().getCustomer(), scheduledTime);
        return bookingRepository.save(booking);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BookingResponse toResponse(Booking booking) {
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