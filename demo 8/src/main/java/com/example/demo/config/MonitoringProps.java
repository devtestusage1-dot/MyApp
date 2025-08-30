package com.example.demo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "monitoring")
public class MonitoringProps {

    private Actuator actuator = new Actuator();
    private Schedule schedule = new Schedule();
    private List<MetricSpec> metrics;

    public Actuator getActuator() { return actuator; }
    public Schedule getSchedule() { return schedule; }
    public List<MetricSpec> getMetrics() { return metrics; }

    public void setMetrics(List<MetricSpec> metrics) { this.metrics = metrics; }

    public static class Actuator {
        private String baseUrl;
        private long timeoutMs = 3000L;

        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

        public long getTimeoutMs() { return timeoutMs; }
        public void setTimeoutMs(long timeoutMs) { this.timeoutMs = timeoutMs; }
    }

    public static class Schedule {
        private String cron = "0 * * * * *";
        public String getCron() { return cron; }
        public void setCron(String cron) { this.cron = cron; }
    }

    public static class MetricSpec {
        private String name;
        private Map<String, String> tags; // optional

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Map<String, String> getTags() { return tags; }
        public void setTags(Map<String, String> tags) { this.tags = tags; }
    }
}
