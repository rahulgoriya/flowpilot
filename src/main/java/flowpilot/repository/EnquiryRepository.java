package flowpilot.repository;

import flowpilot.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    List<Enquiry> findByCustomerId(Long customerId);

    List<Enquiry> findAllByOrderByCreatedAtDesc();

}