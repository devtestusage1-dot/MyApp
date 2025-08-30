package com.example.demo.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
public class MetricsLogger {

    private static final Logger log = LoggerFactory.getLogger(MetricsLogger.class);
    private final RestTemplate rest = new RestTemplate();

    @Value("${monitoring.base-url:}")
    private String baseUrl;

    @Value("${monitoring.metrics:}")
    private String metricsCsv;

    private static String toPlain(JsonNode v) {
        if (v == null || v.isMissingNode() || v.isNull()) return "N/A";
        if (!v.isNumber()) return v.asText();
        if (v.isIntegralNumber()) return v.bigIntegerValue().toString();
        return v.decimalValue().toPlainString();
    }

    @Scheduled(fixedRateString = "${monitoring.interval-ms:300000}")
    public void logMetrics() {
        if (metricsCsv == null || metricsCsv.isBlank()) {
            log.warn("event=metrics_skip reason=no_config ts={}", Instant.now());
            return;
        }

        final String base = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        for (String metric : metricsCsv.split("\\s*,\\s*")) {
            if (metric == null || metric.isBlank()) continue;
            final String url = base + metric;

            try {
                JsonNode root = rest.getForObject(url, JsonNode.class);
                JsonNode valueNode =
                        root.path("measurements")
                                    .path(0).path("value");
                String unit = root.path("baseUnit").asText("value");
                String ts = Instant.now().toString();

                if ("system.cpu.usage".equals(metric)) {
                    String pct = String.format("%.2f", valueNode.asDouble() * 100);
                    log.info("event=metric metric={} value={} unit=percent source=actuator ts={} url={}",
                            metric, pct, ts, url);
                } else {
                    log.info("event=metric metric={} value={} unit={} source=actuator ts={} url={}",
                            metric, toPlain(valueNode), unit, ts, url);
                }
            } catch (Exception e) {
                log.error("event=metric_fail metric={} ts={} url={} reason=\"{}\"",
                        metric, Instant.now(), url, e.getMessage());
            }
        }
    }
}