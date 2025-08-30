package com.example.demo.client;
import com.example.demo.dto.ActuatorMetricResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ActuatorMetricsClient {

    private static final Logger log = LoggerFactory.getLogger(ActuatorMetricsClient.class);
    private final WebClient webClient;

    public ActuatorMetricsClient(WebClient actuatorWebClient) {
        this.webClient = actuatorWebClient;
    }

    /**
     * Fetch metric single numeric value. If multiple measurements, prefer first VALUE.
     * Supports tag filters via /metrics/{metric}?tag=k:v&tag=a:b
     */
    public Optional<Double> fetchValue(String metricName, Map<String, String> tags) {
        try {
            ActuatorMetricResponse resp = webClient
                    .get()
                    .uri(uriBuilder -> {
                        var b = uriBuilder.path("/metrics/{metric}");
                        if (tags != null && !tags.isEmpty()) {
                            // /metrics/{metric}?tag=k:v&tag=a:b ...
                            tags.forEach((k, v) -> b.queryParam("tag", k + ":" + v));
                        }
                        // IMPORTANT: Pass metricName to build() that contains {metric}
                        return b.build(metricName);
                    })
                    .retrieve()
                    .bodyToMono(ActuatorMetricResponse.class)
                    .block();

            if (resp == null || resp.getMeasurements() == null || resp.getMeasurements().isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(resp.getMeasurements().get(0).getValue());
        } catch (Exception ex) {
            log.warn("Failed to fetch metric={} tags={}: {}", metricName, tags, ex.toString());
            return Optional.empty();
        }
    }


    public List<String> listAllMetricNames() {
        try {
            var root = webClient.get().uri("/metrics").retrieve().bodyToMono(Map.class).block();
            if (root == null) return List.of();
            Object names = root.get("names");
            if (names instanceof List<?> list) {
                List<String> out = new ArrayList<>();
                for (Object o : list) if (o instanceof String s) out.add(s);
                return out;
            }
        } catch (Exception ex) {
            log.warn("Failed to list metrics: {}", ex.toString());
        }
        return List.of();
    }
}
