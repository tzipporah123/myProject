package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.rest.ex.CouponAlreadyExistsException;
import com.jb.CouponSystem.rest.ex.CouponNoExistsException;
import com.jb.CouponSystem.rest.ex.CustomerNoExistsException;
import com.jb.CouponSystem.rest.ex.InvalidLoginException;
import com.jb.CouponSystem.rest.model.CustomerErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@CrossOrigin(origins = "http://localhost:4200")
@RestControllerAdvice
public class CustomerControllerAdvice {

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CustomerErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return CustomerErrorResponse.ofNow(ex.getMessage(), 213456);
    }

    @ExceptionHandler(CustomerNoExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomerErrorResponse handleNotFound(CustomerNoExistsException ex) {
        return CustomerErrorResponse.ofNow(ex.getMessage(), 545311);
    }

    @ExceptionHandler(CouponNoExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomerErrorResponse handleNotFound(CouponNoExistsException ex) {
        return CustomerErrorResponse.ofNow(ex.getMessage(), 545312);
    }

    @ExceptionHandler(CouponAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public CustomerErrorResponse handleNotAcceptable(CouponAlreadyExistsException ex) {
        return CustomerErrorResponse.ofNow(ex.getMessage(), 874656);
    }
}
