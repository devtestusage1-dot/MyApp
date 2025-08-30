package com.example.demo.config;


import com.example.demo.client.ActuatorMetricsClient;
import com.example.demo.config.MonitoringProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MetricsConfigValidator {

    private static final Logger log = LoggerFactory.getLogger(MetricsConfigValidator.class);

    private final ActuatorMetricsClient client;
    private final MonitoringProps props;

    public MetricsConfigValidator(ActuatorMetricsClient client, MonitoringProps props) {
        this.client = client;
        this.props = props;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void validateConfiguredMetrics() {
        log.info("Validating configured metrics against Actuator exposure...");

        List<String> availableMetrics = client.listAllMetricNames();
        if (availableMetrics.isEmpty()) {
            log.warn("Actuator /metrics returned empty list — is Actuator enabled and exposure configured?");
            return;
        }

        Set<String> availableSet = new HashSet<>(availableMetrics);
        props.getMetrics().forEach(spec -> {
            String metricName = spec.getName();
            if (!availableSet.contains(metricName)) {
                log.warn("Configured metric '{}' is NOT available in Actuator metrics list. " +
                        "It may cause 404 until enabled or removed.", metricName);
            } else {
                log.debug("Metric '{}' is available.", metricName);
            }
        });

        log.info("Available metrics sample: {}", availableMetrics.stream().limit(10).toList());
    }
}

