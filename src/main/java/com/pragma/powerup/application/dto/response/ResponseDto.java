package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResponseDto {
    private boolean error;
    private String message;
    private Object data;

}
