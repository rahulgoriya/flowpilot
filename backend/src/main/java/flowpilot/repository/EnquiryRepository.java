package flowpilot.repository;

import flowpilot.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    @Query("SELECT e FROM Enquiry e JOIN FETCH e.customer WHERE e.customer.id = :customerId")
    List<Enquiry> findByCustomerIdWithCustomer(@Param("customerId") Long customerId);

    @Query("SELECT e FROM Enquiry e JOIN FETCH e.customer ORDER BY e.createdAt DESC")
    List<Enquiry> findAllWithCustomerOrderByCreatedAtDesc();

    @Query("SELECT e FROM Enquiry e JOIN FETCH e.customer WHERE e.id = :id")
    Optional<Enquiry> findByIdWithCustomer(@Param("id") Long id);

}