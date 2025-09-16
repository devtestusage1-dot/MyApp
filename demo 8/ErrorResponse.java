package com.test.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    String code;
    String message;
    String target;
}
