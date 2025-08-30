package com.example.demo.service;

import com.example.demo.dto.BaseEvent;
import com.example.demo.dto.KafkaPublishEvent;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

  private static final Logger log = LoggerFactory.getLogger(KafkaServiceImpl.class);
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${kafka.dlq.topic:}")
  private String dlqTopic;

  public KafkaServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @EventListener
  @Override
  public void publishMessage(KafkaPublishEvent<BaseEvent> event) {
    ProducerRecord<String, Object> producerRecord =
        new ProducerRecord<>(event.getTopic(), event.getKey(), event.getPayload());

    try {
      CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(producerRecord);

      future.whenComplete(
          (result, ex) -> {
            if (ex == null) {
              var metadata = result.getRecordMetadata();
              log.info(
                  "Message sent successfully: topic={} partition={} offset={} key={} value={}",
                  metadata.topic(),
                  metadata.partition(),
                  metadata.offset(),
                  event.getKey(),
                  event.getPayload());
            } else {
              log.error(
                  "Failed to send message: topic={} key={} value={} cause={}",
                  event.getTopic(),
                  event.getKey(),
                  event.getPayload(),
                  ex.getMessage(),
                  ex);
            }
          });
    } catch (Exception e) {
      log.error("Fail to send the message to topic : {} ", event.getTopic(), e);
      sendToDLQ(event.getKey(), event.getPayload());
    }
  }

  private <T> void sendToDLQ(String key, T value) {
    try {
      kafkaTemplate
          .send(dlqTopic, key, value)
          .whenComplete(
              (res, ex) -> {
                if (ex == null) {
                  log.warn(
                      "➡️ Message sent to DLQ={} key={} value={} reason={}",
                      dlqTopic,
                      key,
                      value,
                      "Fail to publish");
                } else {
                  log.error("❌ Failed to send to DLQ={} key={} value={}", dlqTopic, key, value, ex);
                }
              });
    } catch (Exception e) {
      log.error("❌ Exception while sending to DLQ", e);
    }
  }
}
