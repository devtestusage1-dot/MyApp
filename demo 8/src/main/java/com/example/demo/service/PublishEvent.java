package com.example.demo.service;

public interface PublishEvent {

  <T> void publishEvent(String topic, String key, T payload);
}
