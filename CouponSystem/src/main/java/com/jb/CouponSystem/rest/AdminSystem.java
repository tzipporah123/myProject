package com.jb.CouponSystem.rest;

import com.jb.CouponSystem.rest.ex.InvalidLoginException;
import org.springframework.stereotype.Service;

@Service
public class AdminSystem {
    private static final long ADMIN_ID = 1;

    public ClientSession createSession(String email, String password) throws InvalidLoginException {
        if ("admin@gmail.com".equals(email) && "1234".equals(password)) {
            return ClientSession.create(ADMIN_ID, 1);
        }
        throw new InvalidLoginException("Unable to login with provided credentials!");
    }
}
