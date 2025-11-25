package com.dgsw.eodegatno.dto.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends Response {
    private final T data;

    public DataResponse(int status, String message, T data) {
        super(status, message);
        this.data = data;
    }
}
