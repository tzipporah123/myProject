package com.jb.CouponSystem.rest;

public class ClientSession {
    private long clientId;
    private long lastAccessedMillis;
    private int loginType;

    private ClientSession(long clientId, long currentTimeMillis, int loginType) {
        this.clientId = clientId;
        this.lastAccessedMillis = currentTimeMillis;
        this.loginType = loginType;
    }

    public long getClientId() {
        return clientId;
    }

    public int getLoginType() {
        return loginType;
    }

    public static ClientSession create(long clientId, int loginType) {
        return new ClientSession(clientId, System.currentTimeMillis(), loginType);
    }

    public long getLastAccessedMillis() {
        return lastAccessedMillis;
    }

    public void access() {
        lastAccessedMillis = System.currentTimeMillis();
    }
}
