package com.jb.CouponSystem.rest;

import com.jb.CouponSystem.data.Entity.Customer;
import com.jb.CouponSystem.rest.ex.InvalidLoginException;
import com.jb.CouponSystem.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerSystem {

    private final CustomerServiceImpl customerService;

    @Autowired
    public CustomerSystem(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    public ClientSession createSession(String email, String password) throws InvalidLoginException {
        Optional<Customer> optCustomer = customerService.findByEmailAndPassword(email, password);
        if (optCustomer.isPresent()) {
            return ClientSession.create(optCustomer.get().getId(), 3);
        }
        throw new InvalidLoginException("Unable to login with provided credentials!");
    }
}
