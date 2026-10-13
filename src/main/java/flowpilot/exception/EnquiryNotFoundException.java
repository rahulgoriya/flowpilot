package flowpilot.exception;

public class EnquiryNotFoundException extends RuntimeException {

    public EnquiryNotFoundException(Long id) {
        super("Enquiry not found with id: " + id);
    }
}