package com.example.demo.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService implements HealthIndicator {

  private final MeterRegistry registry;

  public HealthService(HealthContributorRegistry registry) {
    this.registry = new SimpleMeterRegistry();
  }


  @Override
  public Health health() {
    double value = registry.get("system.cpu.usage").gauge().value();
    return Health.up().withDetail("system.cpu.usage", value).build();
  }
}
