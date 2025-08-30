package com.example.demo.service;

import com.example.demo.dto.BaseEvent;
import com.example.demo.dto.KafkaPublishEvent;

public interface KafkaService {

  void publishMessage(KafkaPublishEvent<BaseEvent> kafkaPublishEvent);
}
