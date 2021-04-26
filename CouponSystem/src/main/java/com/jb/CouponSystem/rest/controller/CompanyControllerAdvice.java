package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.rest.ex.*;
import com.jb.CouponSystem.rest.model.CompanyErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@CrossOrigin(origins = "http://localhost:4200")
@RestControllerAdvice
public class CompanyControllerAdvice {
    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CompanyErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return CompanyErrorResponse.ofNow(ex.getMessage(), 213458);
    }

    @ExceptionHandler(CompanyNoExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CompanyErrorResponse handleNotFound(CompanyNoExistsException ex) {
        return CompanyErrorResponse.ofNow(ex.getMessage(), 545314);
    }

    @ExceptionHandler(CouponNoExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CompanyErrorResponse handleNotFound(CouponNoExistsException ex) {
        return CompanyErrorResponse.ofNow(ex.getMessage(), 545313);
    }

    @ExceptionHandler(InvalidUpdateException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CompanyErrorResponse handleUnauthorized(InvalidUpdateException ex) {
        return CompanyErrorResponse.ofNow(ex.getMessage(), 213455);
    }

    @ExceptionHandler(CouponAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public CompanyErrorResponse handleNotAcceptable(CouponAlreadyExistsException ex) {
        return CompanyErrorResponse.ofNow(ex.getMessage(), 874657);
    }
}
