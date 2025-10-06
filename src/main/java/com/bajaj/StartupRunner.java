package com.bajaj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Component
public class StartupRunner implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        String requestBody = """
                {
                    "name": "John Doe",
                    "regNo": "REG12347",
                    "email": "john@example.com"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("Failed to generate webhook: " + response.getStatusCode());
            return;
        }

       
        JsonNode json = objectMapper.readTree(response.getBody());
        String webhookUrl = json.get("webhook").asText();
        String accessToken = json.get("accessToken").asText();

        System.out.println(" Webhook generated");
        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("AccessToken: " + accessToken);

       
        String finalQuery = "SELECT * FROM your_table WHERE condition = true;";

        String submitBody = """
                {
                    "finalQuery": "%s"
                }
                """.formatted(finalQuery);

        HttpHeaders submitHeaders = new HttpHeaders();
        submitHeaders.setContentType(MediaType.APPLICATION_JSON);
        submitHeaders.setBearerAuth(accessToken);

        HttpEntity<String> submitEntity = new HttpEntity<>(submitBody, submitHeaders);
        ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, submitEntity, String.class);

        System.out.println("✅ Submission Response: " + submitResponse.getBody());
    }
}
