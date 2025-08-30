package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.example.demo.dto.KafkaPublishEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class KafkaPublishEventImplTest {

  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks private KafkaPublishEventImpl kafkaPublishEvent;

  @Test
  void testPublishEvent_publishesKafkaEvent() {
    String topic = "test-topic";
    String key = "key-123";
    String value = "payload-data";

    kafkaPublishEvent.publishEvent(topic, key, value);

    ArgumentCaptor<KafkaPublishEvent<?>> captor = ArgumentCaptor.forClass(KafkaPublishEvent.class);

    verify(eventPublisher).publishEvent(captor.capture());

    KafkaPublishEvent<?> captured = captor.getValue();

    assertThat(captured.getTopic()).isEqualTo(topic);
    assertThat(captured.getKey()).isEqualTo(key);
    assertThat(captured.getPayload()).isEqualTo(value);
  }
}
