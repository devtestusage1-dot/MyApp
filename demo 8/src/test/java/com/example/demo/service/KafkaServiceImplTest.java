package com.example.demo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.*;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@ExtendWith(MockitoExtension.class)
class KafkaServiceImplTest {

  @Mock private KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks private KafkaServiceImpl kafkaService;

  private BaseEvent baseEvent;

  @BeforeEach
  void setUp() {
    baseEvent =
        StartEvent.builder()
            .timeStamp(Instant.now().toString())
            .callId("call_id")
            .ani("ani")
            .applicationName("app_name")
            .eventType(EventType.CALL_START.getValue())
            .build();
  }

  @Test
  void testPublishMessage_success() {
    KafkaPublishEvent<BaseEvent> event =
        new KafkaPublishEvent<>(this, "test-topic", "key1", baseEvent);

    RecordMetadata metadata =
        new RecordMetadata(
            new org.apache.kafka.common.TopicPartition("test-topic", 0),
            0,
            1,
            System.currentTimeMillis(),
            0L,
            0,
            0);
    SendResult<String, Object> sendResult =
        new SendResult<>(new ProducerRecord<>("test-topic", "key1", baseEvent), metadata);

    CompletableFuture<SendResult<String, Object>> future =
        CompletableFuture.completedFuture(sendResult);
    when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

    kafkaService.publishMessage(event);

    verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
  }

  @Test
  void testPublishMessage_failureInCallback() {
    KafkaPublishEvent<BaseEvent> event =
        new KafkaPublishEvent<>(this, "test-topic", "key2", baseEvent);

    CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException("Kafka send failed"));
    when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

    kafkaService.publishMessage(event);

    verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
  }

  @Test
  void testPublishMessage_exceptionBeforeFuture() {
    KafkaPublishEvent<BaseEvent> event =
        new KafkaPublishEvent<>(this, "bad-topic", "key3", baseEvent);

    when(kafkaTemplate.send(any(ProducerRecord.class)))
        .thenThrow(new RuntimeException("Producer failure"));

    kafkaService.publishMessage(event);

    verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
  }

  @Test
  void testSendToDLQ_success() {
    CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(null);
    when(kafkaTemplate.send(anyString(), anyString(), any())).thenReturn(future);

    kafkaService.publishMessage(new KafkaPublishEvent<>(this, "topic", "dlq-key", baseEvent));

    verify(kafkaTemplate, atLeastOnce()).send(any(ProducerRecord.class));
  }

  @Test
  void testSendToDLQ_failureInCallback() {
    CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException("DLQ failed"));

    when(kafkaTemplate.send(anyString(), anyString(), any())).thenReturn(future);

    kafkaService.publishMessage(new KafkaPublishEvent<>(this, "topic", "dlq-key2", baseEvent));

    verify(kafkaTemplate, atLeastOnce()).send(any(ProducerRecord.class));
  }

  @Test
  void testSendToDLQ_exceptionImmediately() {
    when(kafkaTemplate.send(anyString(), anyString(), any()))
        .thenThrow(new RuntimeException("Immediate DLQ exception"));

    kafkaService.publishMessage(new KafkaPublishEvent<>(this, "topic", "dlq-key3", baseEvent));

    verify(kafkaTemplate, atLeastOnce()).send(any(ProducerRecord.class));
  }
}
