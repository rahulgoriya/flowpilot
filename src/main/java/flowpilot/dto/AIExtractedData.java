package flowpilot.dto;

public class AIExtractedData {
    
    private String serviceType;
    private String propertyType;
    private Integer bedrooms;
    private String location;
    private String preferredDateTime;
    private String intent;

    public AIExtractedData() {}

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer bedrooms) { this.bedrooms = bedrooms; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPreferredDateTime() { return preferredDateTime; }
    public void setPreferredDateTime(String preferredDateTime) { this.preferredDateTime = preferredDateTime; }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
}