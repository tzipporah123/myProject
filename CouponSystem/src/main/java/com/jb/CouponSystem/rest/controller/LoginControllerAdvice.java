package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.rest.ex.InvalidLoginException;
import com.jb.CouponSystem.rest.model.CompanyErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@CrossOrigin(origins = "http://localhost:4200")
@RestControllerAdvice
public class LoginControllerAdvice {
    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CompanyErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return CompanyErrorResponse.ofNow(ex.getMessage(), 213458);
    }
}
