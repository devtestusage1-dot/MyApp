package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

  @NotNull
  @JsonProperty("event_type")
  private String eventType;

  @NotNull
  @JsonProperty("call_id")
  private String callId;

  @JsonProperty("ANI")
  private String ani;

  @JsonProperty("application_name")
  private String applicationName;

  private String timeStamp;

  private String language;
  private String duration;
  private String exitReason;

  @JsonProperty("prompt_id_AtExit")
  private String promptIdAtExit;

  @JsonProperty("prompt_description")
  private String promptDescription;
}
