package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.rest.ex.*;
import com.jb.CouponSystem.rest.model.AdminErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminControllerAdvice {
    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AdminErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return AdminErrorResponse.ofNow(ex.getMessage(), 213458);
    }

    @ExceptionHandler(CompanyNoExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AdminErrorResponse handleNotFound(CompanyNoExistsException ex) {
        return AdminErrorResponse.ofNow(ex.getMessage(), 545314);
    }

    @ExceptionHandler(CouponNoExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AdminErrorResponse handleNotFound(CouponNoExistsException ex) {
        return AdminErrorResponse.ofNow(ex.getMessage(), 545313);
    }

    @ExceptionHandler(InvalidUpdateException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AdminErrorResponse handleUnauthorized(InvalidUpdateException ex) {
        return AdminErrorResponse.ofNow(ex.getMessage(), 213455);
    }

    @ExceptionHandler(CouponAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public AdminErrorResponse handleNotAcceptable(CouponAlreadyExistsException ex) {
        return AdminErrorResponse.ofNow(ex.getMessage(), 874657);
    }

    @ExceptionHandler(CustomerNoExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AdminErrorResponse handleNotFound(CustomerNoExistsException ex) {
        return AdminErrorResponse.ofNow(ex.getMessage(), 545311);
    }
}
