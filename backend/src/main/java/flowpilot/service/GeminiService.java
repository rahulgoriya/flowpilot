package flowpilot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import flowpilot.dto.AIExtractedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    // Try these models in order
    private final String[] models = {
        "gemini-3.8-flash",
        "gemini-3.1-flash-lite",
        "gemini-2.0-flash"
    };

    public GeminiService() {
        // NEW: Timeout set karo taaki API atke nahi
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000); // 10 seconds
        requestFactory.setReadTimeout(10000);    // 10 seconds
        
        this.restClient = RestClient.builder().requestFactory(requestFactory).build();
        this.objectMapper = new ObjectMapper();
    }

    public AIExtractedData extractEnquiryData(String customerMessage) {
        String prompt = buildPrompt(customerMessage);
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        // 1. Try AI Models with Retries
        for (String model : models) {
            for (int attempt = 1; attempt <= 2; attempt++) {
                try {
                    log.info("Attempting AI extraction using model: {} (Attempt {})", model, attempt);
                    String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";
                    
                    String rawResponse = restClient.post()
                            .uri(apiUrl)
                            .header("x-goog-api-key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(requestBody)
                            .retrieve()
                            .body(String.class);

                    JsonNode root = objectMapper.readTree(rawResponse);
                    String aiJsonString = root.path("candidates").get(0)
                            .path("content").path("parts").get(0)
                            .path("text").asText();

                    AIExtractedData data = objectMapper.readValue(aiJsonString, AIExtractedData.class);
                    data.setRawJsonResponse(aiJsonString);
                    return data;

                } catch (Exception e) {
                    log.warn("Model {} failed on attempt {}: {}", model, attempt, e.getMessage());
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
            }
        }

        // 2. Fallback: If AI fails completely, extract keywords manually
        log.error("All AI models failed. Using Keyword Fallback.");
        return fallbackExtraction(customerMessage);
    }

    private AIExtractedData fallbackExtraction(String message) {
        AIExtractedData data = new AIExtractedData();
        String lower = message.toLowerCase();

        if (lower.contains("1bhk")) data.setPropertyType("1BHK");
        else if (lower.contains("2bhk")) data.setPropertyType("2BHK");
        else if (lower.contains("3bhk")) data.setPropertyType("3BHK");
        else if (lower.contains("4bhk")) data.setPropertyType("4BHK");
        else if (lower.contains("office")) data.setPropertyType("office");

        if (lower.contains("deep clean")) data.setServiceType("deep clean");
        else if (lower.contains("regular")) data.setServiceType("regular clean");
        else if (lower.contains("sofa")) data.setServiceType("sofa cleaning");
        else if (lower.contains("office")) data.setServiceType("office cleaning");

        if (lower.contains("quote") || lower.contains("price")) data.setIntent("inquiry");
        else if (lower.contains("angry") || lower.contains("complaint")) data.setIntent("complaint");
        else data.setIntent("booking");

        data.setRawJsonResponse("{\"fallback\": true}");
        return data;
    }

    private String buildPrompt(String message) {
        return """
                You are a data extraction assistant for a cleaning business.
                Extract the following fields from the customer message below.
                Respond ONLY with a valid JSON object. Do not include markdown formatting, backticks, or any extra text.
                
                Fields:
                - serviceType (string: "deep clean", "regular clean", "sofa cleaning", "office cleaning", or null)
                - propertyType (string: "1BHK", "2BHK", "3BHK", "4BHK", "office", or null)
                - bedrooms (integer or null)
                - location (string or null)
                - preferredDateTime (string or null)
                - intent (string: "booking", "inquiry", "complaint", or "other")
                
                Customer message: "%s"
                """.formatted(message);
    }
}