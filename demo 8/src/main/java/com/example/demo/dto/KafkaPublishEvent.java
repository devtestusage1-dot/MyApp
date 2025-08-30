package com.example.demo.dto;

import org.springframework.context.ApplicationEvent;

public class KafkaPublishEvent<T> extends ApplicationEvent {
  private final String topic;
  private final String key;
  private final T payload;

  public KafkaPublishEvent(Object source, String topic, String key, T payload) {
    super(source);
    this.topic = topic;
    this.key = key;
    this.payload = payload;
  }

  public String getTopic() {
    return topic;
  }

  public String getKey() {
    return key;
  }

  public T getPayload() {
    return payload;
  }
}
