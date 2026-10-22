package flowpilot.service;

import flowpilot.dto.PriceCalculation;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    public PriceCalculation calculatePrice(String propertyType, String serviceType) {
        int base = calculateBasePrice(propertyType);
        int addOn = calculateAddOn(serviceType);
        int total = base + addOn;

        String breakdown = String.format("Base (%s): ₹%d + Add-on (%s): ₹%d", 
                propertyType != null ? propertyType : "Default", base, 
                serviceType != null ? serviceType : "None", addOn);

        return new PriceCalculation(base, addOn, total, breakdown);
    }

    private int calculateBasePrice(String propertyType) {
        if (propertyType == null) return 2000;
        
        return switch (propertyType.toUpperCase()) {
            case "1BHK" -> 1500;
            case "2BHK" -> 2500;
            case "3BHK" -> 3500;
            case "4BHK" -> 4500;
            case "OFFICE" -> 5000;
            default -> 2000; // Fallback for unknown
        };
    }

    private int calculateAddOn(String serviceType) {
        if (serviceType == null) return 0;

        return switch (serviceType.toLowerCase()) {
            case "deep clean" -> 1000;
            case "sofa cleaning" -> 500;
            case "regular clean" -> 0;
            default -> 0;
        };
    }
}