package com.jb.CouponSystem.rest;

import com.jb.CouponSystem.data.Entity.Company;
import com.jb.CouponSystem.rest.ex.InvalidLoginException;
import com.jb.CouponSystem.service.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanySystem {
    private final CompanyServiceImpl companyService;

    @Autowired
    public CompanySystem(CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }

    public ClientSession createSession(String email, String password) throws InvalidLoginException {
        Optional<Company> optCompany = companyService.findByEmailAndPassword(email, password);
        if (optCompany.isPresent()) {
            return ClientSession.create(optCompany.get().getId(), 2);
        }
        throw new InvalidLoginException("Unable to login with provided credentials!");
    }
}
