package com.example.demo.service;

import com.example.demo.dto.EventRequest;

public interface EventService {
  void publishEvent(EventRequest eventRequest);
}
