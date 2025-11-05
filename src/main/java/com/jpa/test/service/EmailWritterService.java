package com.jpa.test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpa.test.EmailRequest;

@Service
public class EmailWritterService {

    private final WebClient webClient;
    private final String apikey;

    public EmailWritterService(WebClient.Builder webClientBuilder,
                               @Value("${gemini.api.url}") String baseUrl,
                               @Value("${gemini.api.key}") String geminiApiKey) {
        this.apikey = geminiApiKey;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public String generateEmailReply(EmailRequest emailRequest) {

        String prompt = buildprompt(emailRequest);

        String reqBody = String.format("""
                {
                    "contents": [
                      {
                        "parts": [
                          {
                            "text": "%s"
                          }
                        ]
                      }
                    ]
                  }
                """, prompt);

        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.0-flash:generateContent")
                        .build())
                .header("x-goog-api-key", apikey)
                .header("Content-Type", "application/json")
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractReplyFromResponse(response);
    }

	private String extractReplyFromResponse(String response) {
		
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root=mapper.readTree(response);
			return root.path("candidates")
			.get(0)
			.path("content")
			.path("parts")
			.get(0)
			.path("text")
			.asText();
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private String buildprompt(EmailRequest emailRequest) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Generate a professional email reply for the following email:");
		if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()) {
			prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
		}
	prompt.append("Original Email :\n").append(emailRequest.getEmailcontent());
	return prompt.toString();
	}
	
	
	

}
