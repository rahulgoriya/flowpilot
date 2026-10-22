package flowpilot.dto;

public class PriceCalculation {
    
    private Integer basePrice;
    private Integer addOnPrice;
    private Integer totalPrice;
    private String breakdown; // e.g., "Base 2BHK: 2500 + Deep Clean: 1000"

    public PriceCalculation(Integer basePrice, Integer addOnPrice, Integer totalPrice, String breakdown) {
        this.basePrice = basePrice;
        this.addOnPrice = addOnPrice;
        this.totalPrice = totalPrice;
        this.breakdown = breakdown;
    }

    public Integer getBasePrice() { return basePrice; }
    public Integer getAddOnPrice() { return addOnPrice; }
    public Integer getTotalPrice() { return totalPrice; }
    public String getBreakdown() { return breakdown; }
}