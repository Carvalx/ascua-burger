package com.carvalx.ascua_burger.ia.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

// Adaptador para la API de Groq
// Aislamos la llamada HTTP aquí para poder mockearla en tests
// Si mañana cambiamos de Groq a OpenAI solo tocamos este fichero
@Component
@RequiredArgsConstructor
public class GroqApiClient {

    private final WebClient webClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String model;

    public String completar(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "Eres el asistente de Ascua Burger. " +
                                        "Recomiendas hamburguesas basándote en las preferencias del cliente. " +
                                        "Responde SOLO con JSON válido, sin texto adicional ni backticks."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7,
                "max_tokens", 500
        );

        Map response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map> choices = (List<Map>) response.get("choices");
        Map message = (Map) choices.get(0).get("message");
        return (String) message.get("content");
    }
}