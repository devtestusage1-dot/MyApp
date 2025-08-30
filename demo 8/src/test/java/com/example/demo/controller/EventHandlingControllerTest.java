package com.example.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.example.demo.dto.EventRequest;
import com.example.demo.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EventHandlingControllerTest {

  @Mock private EventService eventService;

  @InjectMocks private EventHandlingController controller;

  @Test
  void testHandleEvent_success() {
    EventRequest event = new EventRequest();
    event.setEventType("event");
    event.setAni("ani");
    event.setApplicationName("app_name");
    event.setCallId("call_id");

    doNothing().when(eventService).publishEvent(any(EventRequest.class));

    ResponseEntity<?> response = controller.handleEvent(event);

    assertThat(response.getStatusCode().value()).isEqualTo(200);
    verify(eventService).publishEvent(any(EventRequest.class));
  }
}
