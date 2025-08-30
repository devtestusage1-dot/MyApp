package com.example.demo.jobs;


import com.example.demo.client.ActuatorMetricsClient;
import com.example.demo.config.MonitoringProps;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.search.RequiredSearch;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MetricsPoller {

    private static final Logger log = LoggerFactory.getLogger(MetricsPoller.class);
    private final ActuatorMetricsClient client;
    private final MonitoringProps props;

    private final MeterRegistry meterRegistry;

    public MetricsPoller(ActuatorMetricsClient client, MonitoringProps props, MeterRegistry meterRegistry) {
        this.client = client;
        this.props = props;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    

    // Use cron from config via SpEL is possible, but simpler to keep fixed here and rely on props for control
  //  @Scheduled(cron = "#{@monitoringScheduleCron}")
    public void pollAndLog() {

        meterRegistry.getMeters().forEach(meter -> {
            System.out.println(meter.getId().getName());
        });

        props.getMetrics().forEach(spec -> {
            RequiredSearch requiredSearch = meterRegistry.get(spec.getName());
            System.out.println("Values : "+requiredSearch.gauge().value());
        });

        /*Map<String, Object> snapshot = new LinkedHashMap<>();

        props.getMetrics().forEach(spec -> {
            var value = client.fetchValue(spec.getName(), spec.getTags()).orElse(null);
            // Format CPU like percentage if it ends with ".cpu.usage"
            if (spec.getName().endsWith(".cpu.usage") && value != null) {
                snapshot.put(spec.getName(), String.format("%.2f%%", value * 100.0));
            } else {
                snapshot.put(spec.getName(), value);
            }
        });

        // Structured single line for easy scraping/alerting
        // Example: metric=system.cpu.usage value=12.34% ...
        StringBuilder sb = new StringBuilder("metrics-snapshot");
        snapshot.forEach((k, v) -> sb.append(' ').append(k).append('=').append(v));
        log.info(sb.toString());*/
    }
}
