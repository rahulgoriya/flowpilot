package flowpilot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import flowpilot.dto.AIExtractedData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiService() {
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }

    public AIExtractedData extractEnquiryData(String customerMessage) {
        String prompt = buildPrompt(customerMessage);

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        try {
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

            return objectMapper.readValue(aiJsonString, AIExtractedData.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract AI data: " + e.getMessage(), e);
        }
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
                
                JSON Output:
                """.formatted(message);
    }
}