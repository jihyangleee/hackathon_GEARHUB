package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class SummarizationService {

    private static final Logger log = LoggerFactory.getLogger(SummarizationService.class);

    private final String endpoint;
    private final String apiKey;
    private final String openAiKey;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public SummarizationService(@Value("${app.gemini.endpoint:}") String endpoint,
                                @Value("${app.gemini.apiKey:}") String apiKey,
                                @Value("${GEMINI_API_KEY:}") String envGeminiKey,
                                @Value("${app.openai.apiKey:}") String openAiProp,
                                @Value("${OPENAI_API_KEY:}") String openAiEnv) {
        this.endpoint = endpoint == null ? "" : endpoint.trim();
        String keyFromProps = apiKey == null ? "" : apiKey.trim();
        String keyFromEnv = envGeminiKey == null ? "" : envGeminiKey.trim();
        // prefer explicit property, else environment GEMINI_API_KEY
        this.apiKey = !keyFromProps.isEmpty() ? keyFromProps : keyFromEnv;
        String openKeyFromProps = openAiProp == null ? "" : openAiProp.trim();
        String openKeyFromEnv = openAiEnv == null ? "" : openAiEnv.trim();
        this.openAiKey = !openKeyFromProps.isEmpty() ? openKeyFromProps : openKeyFromEnv;
    }

    /**
     * Generate a short summary for the provided text.
     * If Gemini endpoint/apiKey not configured, returns a simple heuristic summary.
     */
    public String summarize(String text) {
        if (text == null || text.isBlank()) return "요약할 내용이 없습니다.";

        if (endpoint.isEmpty() || apiKey.isEmpty()) {
            // Fallback: simple heuristic - first 2 sentences or first 220 chars
            return heuristicSummary(text);
        }

        try {
            String prompt = "다음 글을 한국어로 2-3문장으로 요약해줘:\n\n" + text;

            // If endpoint looks like Google Generative Language API, use its contract
            HttpResponse<String> resp;
            if (endpoint.contains("generativelanguage.googleapis.com") || endpoint.contains("googleapis.com")) {
                // model endpoint example: https://generativelanguage.googleapis.com/v1/models/text-bison-001:generateText
                // allow passing API key as query param if provided
                String url = endpoint;
                if (!apiKey.isEmpty()) {
                    if (!url.contains("?")) url += "?key=" + apiKey;
                    else url += "&key=" + apiKey;
                }

                // Build Google GLM payload (construct ObjectNode stepwise to avoid chaining on JsonNode)
                com.fasterxml.jackson.databind.node.ObjectNode rootNode = mapper.createObjectNode();
                com.fasterxml.jackson.databind.node.ObjectNode promptNode = mapper.createObjectNode();
                promptNode.put("text", prompt);
                rootNode.set("prompt", promptNode);
                rootNode.put("maxOutputTokens", 300);
                rootNode.put("temperature", 0.2);
                String payload = rootNode.toString();

                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(20))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .build();

                resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            } else {
                // Generic prompt-based request (user-supplied endpoint)
                String payload = mapper.createObjectNode()
                        .put("prompt", prompt)
                        .put("maxOutputTokens", 300)
                        .put("temperature", 0.2)
                        .toString();

                HttpRequest.Builder b = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .timeout(Duration.ofSeconds(20))
                        .header("Content-Type", "application/json");
                if (!apiKey.isEmpty()) b.header("Authorization", "Bearer " + apiKey);
                HttpRequest req = b.POST(HttpRequest.BodyPublishers.ofString(payload)).build();
                resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            }
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                // Try to parse common response shapes
                JsonNode root = mapper.readTree(resp.body());
                // common field candidates
                // Google GLM response shape: { "candidates": [ { "output": "..." } ], ... }
                if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
                    JsonNode first = root.get("candidates").get(0);
                    if (first.has("content")) return first.get("content").asText();
                    if (first.has("output")) return first.get("output").asText();
                }
                if (root.has("output")) return root.get("output").asText();
                if (root.has("text")) return root.get("text").asText();
                // last-resort: serialize whole body (not ideal)
                return root.toString();
            } else {
                log.warn("Gemini summarization request failed: status={} body={}", resp.statusCode(), resp.body());
                // Try OpenAI fallback if configured
                if (openAiKey != null && !openAiKey.isBlank()) {
                    try {
                        String openRes = callOpenAi(text);
                        if (openRes != null && !openRes.isBlank()) return openRes;
                    } catch (Exception e) {
                        log.warn("OpenAI summarization failed after Gemini failure", e);
                    }
                }
                return heuristicSummary(text);
            }
        } catch (IOException | InterruptedException e) {
            log.warn("Gemini summarization error, falling back to heuristic", e);
            // Try OpenAI if configured
            if (openAiKey != null && !openAiKey.isBlank()) {
                try {
                    String openRes = callOpenAi(text);
                    if (openRes != null && !openRes.isBlank()) return openRes;
                } catch (Exception ex) {
                    log.warn("OpenAI summarization error after Gemini exception", ex);
                }
            }
            return heuristicSummary(text);
        }
    }

    private String callOpenAi(String text) throws IOException, InterruptedException {
        // Use Chat Completions API (gpt-3.5-turbo as default)
        String endpoint = "https://api.openai.com/v1/chat/completions";

        ObjectNode root = mapper.createObjectNode();
        root.put("model", "gpt-3.5-turbo");
        ArrayNode messages = mapper.createArrayNode();
    ObjectNode sys = mapper.createObjectNode();
    sys.put("role", "system");
    // 한국어로 요약을 출력하도록 시스템 메시지를 한국어로 설정
    sys.put("content", "당신은 기술/기사 내용을 간결하게 한국어로 요약하는 도우미입니다.");
        messages.add(sys);
        ObjectNode user = mapper.createObjectNode();
    user.put("role", "user");
    // 사용자 프롬프트도 한국어로 요청
    user.put("content", "다음 글을 한국어로 3-5문장으로 간결하게 요약해줘:\n\n" + text);
        messages.add(user);
        root.set("messages", messages);
        root.put("max_tokens", 200);
        root.put("temperature", 0.2);

        String body = mapper.writeValueAsString(root);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + openAiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
            JsonNode rootNode = mapper.readTree(resp.body());
            JsonNode choices = rootNode.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode msg = choices.get(0).path("message");
                String content = msg.path("content").asText(null);
                if (content != null && !content.isBlank()) return content.trim();
            }
            if (choices.isArray() && choices.size() > 0) {
                String textChoice = choices.get(0).path("text").asText(null);
                if (textChoice != null && !textChoice.isBlank()) return textChoice.trim();
            }
        } else {
            log.warn("OpenAI summarization request failed: status={} body={}", resp.statusCode(), resp.body());
        }
        return null;
    }

    private String heuristicSummary(String text) {
        // split sentences (very naive)
        String[] parts = text.split("(?<=[.!?\n])\\s+");
        if (parts.length >= 2) {
            return parts[0].trim() + " " + parts[1].trim();
        }
        if (text.length() > 220) return text.substring(0, 220).trim() + "...";
        return text;
    }
}
