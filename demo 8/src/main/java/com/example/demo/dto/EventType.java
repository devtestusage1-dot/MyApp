package com.example.demo.dto;

public enum EventType {
  CALL_START("Call_start"),
  IVR_EXIT("IVR_Exit");

  private final String value;

  EventType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
