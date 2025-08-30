package com.example.demo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.example.demo.dto.EventRequest;
import com.example.demo.dto.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

  @Mock private PublishEvent publishEvent;

  @InjectMocks private EventServiceImpl eventService;

  @Test
  void testPublishStartEvent_callsPublishEventWithGeneratedKey() {
    EventRequest startEvent = new EventRequest();
    startEvent.setEventType(EventType.CALL_START.getValue());
    startEvent.setAni("ani");
    startEvent.setApplicationName("app_name");
    startEvent.setCallId("call_id");

    eventService.publishEvent(startEvent);

    verify(publishEvent).publishEvent(any(), any(String.class), any());
  }

  @Test
  void testPublishExitEvent_callsPublishEventWithGeneratedKey() {
    EventRequest exitEvent = new EventRequest();
    exitEvent.setEventType(EventType.IVR_EXIT.getValue());
    exitEvent.setAni("ani");
    exitEvent.setApplicationName("app_name");
    exitEvent.setCallId("call_id");

    eventService.publishEvent(exitEvent);

    verify(publishEvent).publishEvent(any(), any(String.class), any());
  }

  @Test
  void shouldThrowUnsupportedOperationException_WhenInvalidEventReceived() {
    EventRequest unsupportedEvent = new EventRequest();
    unsupportedEvent.setEventType("Not_Supported_Event");
    unsupportedEvent.setAni("ani");
    unsupportedEvent.setApplicationName("app_name");
    unsupportedEvent.setCallId("call_id");

    Assertions.assertThrows(
        UnsupportedOperationException.class, () -> eventService.publishEvent(unsupportedEvent));
  }
}
