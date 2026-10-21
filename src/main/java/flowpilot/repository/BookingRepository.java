package flowpilot.repository;

import flowpilot.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find the booking associated with a specific quote
    Optional<Booking> findByQuoteId(Long quoteId);
}