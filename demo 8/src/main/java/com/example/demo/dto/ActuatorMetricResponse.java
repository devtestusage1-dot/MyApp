package com.example.demo.dto;


import java.util.List;

public class ActuatorMetricResponse {
    private String name;
    private List<Measurement> measurements;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Measurement> getMeasurements() { return measurements; }
    public void setMeasurements(List<Measurement> measurements) { this.measurements = measurements; }

    public static class Measurement {
        private String statistic; // e.g., VALUE
        private Double value;

        public String getStatistic() { return statistic; }
        public void setStatistic(String statistic) { this.statistic = statistic; }

        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
    }
}

