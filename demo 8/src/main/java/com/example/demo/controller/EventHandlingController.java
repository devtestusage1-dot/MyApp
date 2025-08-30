package com.example.demo.controller;

import com.example.demo.dto.EventRequest;
import com.example.demo.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventHandlingController {

  private final EventService eventService;

  public EventHandlingController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping(value = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity handleEvent(@Valid @RequestBody EventRequest eventRequest) {
    eventService.publishEvent(eventRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
