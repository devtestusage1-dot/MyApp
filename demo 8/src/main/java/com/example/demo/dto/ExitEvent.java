package com.example.demo.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExitEvent extends BaseEvent {

  private String language;
  private String duration;
  private String exitReason;
  private String promptIdAtExit;
  private String promptDescription;
}
