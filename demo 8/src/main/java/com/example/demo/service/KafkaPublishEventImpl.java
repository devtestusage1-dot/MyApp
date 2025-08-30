package com.example.demo.service;

import com.example.demo.dto.KafkaPublishEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublishEventImpl implements PublishEvent {

  private final ApplicationEventPublisher eventPublisher;

  public KafkaPublishEventImpl(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public <T> void publishEvent(String topic, String key, T value) {
    eventPublisher.publishEvent(new KafkaPublishEvent<>(this, topic, key, value));
  }
}
