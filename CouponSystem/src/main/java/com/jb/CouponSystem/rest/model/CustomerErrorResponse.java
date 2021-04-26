package com.jb.CouponSystem.rest.model;

public class CustomerErrorResponse {
    private final String message;
    private final long timestamp;
    private final int code;

    private CustomerErrorResponse(String message, long timestamp, int code) {
        this.message = message;
        this.timestamp = timestamp;
        this.code = code;
    }

    public static CustomerErrorResponse ofNow(String message, int code) {
        return new CustomerErrorResponse(message, System.currentTimeMillis(), code);
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getCode() {
        return code;
    }
}
