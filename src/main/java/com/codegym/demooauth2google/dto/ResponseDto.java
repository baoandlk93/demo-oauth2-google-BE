package com.codegym.demooauth2google.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponseDto {
    private String message;
    private Object data;
    private HttpStatus status;
}
