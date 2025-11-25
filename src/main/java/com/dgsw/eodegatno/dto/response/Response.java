package com.dgsw.eodegatno.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class Response {
    private final int status;
    private final String message;

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseEntity<Response> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}
