package com.example.demo.service;

import com.example.demo.dto.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

  private final PublishEvent publishEvent;

  @Value("${event.topic.name:}")
  private String eventTopic;

  public EventServiceImpl(PublishEvent publishEvent) {
    this.publishEvent = publishEvent;
  }

  @Override
  public void publishEvent(EventRequest eventRequest) {
    BaseEvent event = buildEvent(eventRequest);
    publishEvent.publishEvent(eventTopic, UUID.randomUUID().toString(), event);
  }

  private BaseEvent buildEvent(EventRequest eventRequest) {
    if (EventType.CALL_START.getValue().equals(eventRequest.getEventType())) {
      return StartEvent.builder()
          .eventType(eventRequest.getEventType())
          .ani(eventRequest.getAni())
          .applicationName(eventRequest.getApplicationName())
          .callId(eventRequest.getCallId())
          .timeStamp(eventRequest.getTimeStamp())
          .build();
    } else if (EventType.IVR_EXIT.getValue().equals(eventRequest.getEventType())) {
      return ExitEvent.builder()
          .language(eventRequest.getLanguage())
          .duration(eventRequest.getDuration())
          .exitReason(eventRequest.getExitReason())
          .promptIdAtExit(eventRequest.getPromptIdAtExit())
          .promptDescription(eventRequest.getPromptDescription())
          .eventType(eventRequest.getEventType())
          .ani(eventRequest.getAni())
          .applicationName(eventRequest.getApplicationName())
          .callId(eventRequest.getCallId())
          .timeStamp(eventRequest.getTimeStamp())
          .build();
    } else {
      throw new UnsupportedOperationException("Unsupported Request Type received.");
    }
  }
}
