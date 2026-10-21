package flowpilot.repository;

import flowpilot.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    
    List<Quote> findByEnquiryIdOrderByCreatedAtDesc(Long enquiryId);
}