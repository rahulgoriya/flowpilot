package flowpilot.repository;

import flowpilot.entity.Booking;
import flowpilot.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find the booking associated with a specific quote
    Optional<Booking> findByQuoteId(Long quoteId);

    // NEW: Check if a time slot is already booked
    List<Booking> findByScheduledDateTimeAndStatus(LocalDateTime scheduledDateTime, BookingStatus status);
}