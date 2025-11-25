package com.dgsw.eodegatno.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends Response {
    private final String error;

    public ErrorResponse(int status, String message, String error) {
        super(status, message);
        this.error = error;
    }
}
