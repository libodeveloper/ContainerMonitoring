package com.esri.arcgisruntime.container.monitoring.http;

public class ResponseErrorException extends RuntimeException {
    public ResponseErrorException(String msg) {
        super(msg);
    }
}
