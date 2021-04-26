package com.jb.CouponSystem.rest.model;

public class AdminErrorResponse {
    private final String message;
    private final long timestamp;
    private final int code;

    public AdminErrorResponse(String message, long timestamp, int code) {
        this.message = message;
        this.timestamp = timestamp;
        this.code = code;
    }
    public static AdminErrorResponse ofNow(String message, int code) {
        return new AdminErrorResponse(message, System.currentTimeMillis(), code);
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
