package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.data.Entity.Company;
import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.Entity.Customer;
import com.jb.CouponSystem.rest.ClientSession;
import com.jb.CouponSystem.rest.ex.*;
import com.jb.CouponSystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jb.CouponSystem.rest.controller.LoginController.COMPANY;
import static com.jb.CouponSystem.rest.controller.LoginController.CUSTOMER;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CompanySystemController {
    private final CompanyService service;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public CompanySystemController(CompanyService service, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.service = service;
        this.tokensMap = tokensMap;
    }

    @GetMapping("/companies")
    public ResponseEntity<Company> getCompany(@RequestParam String token) {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Company> optCompany = service.getCompanyById(clientSession.getClientId());

        return optCompany.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/companies/coupons")
    public ResponseEntity<List<Coupon>> getCompanyCoupons(@RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> allCoupons;
        allCoupons = service.getCompanyCoupons(clientSession.getClientId());
        if (allCoupons != null && !allCoupons.isEmpty()) {
            return ResponseEntity.ok(allCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/companies/coupons-new-full")
    public ResponseEntity<Coupon> createNewCoupon
            (@RequestBody Coupon coupon,
             @RequestParam String token)
            throws CompanyNoExistsException, CouponAlreadyExistsException, CouponNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optionalCoupon;
        optionalCoupon = service.createCoupon(coupon, coupon.getCompany().getId());

        return optionalCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/companies/coupons-new-empty")
    public ResponseEntity<Coupon> createNewCoupon
            (@RequestParam String title,
             @RequestParam String token)
            throws CompanyNoExistsException, CouponAlreadyExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optionalCoupon = service.createCoupon(clientSession.getClientId(), title);

        return optionalCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/companies/delete-coupon")
    public ResponseEntity<Void> deleteCoupon
            (@RequestParam long couponId,
             @RequestParam String token)
            throws CompanyNoExistsException, CouponNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        service.deleteCoupon(couponId, clientSession.getClientId());
        boolean succeed = true;
        for (Coupon coupon : service.getCompanyCoupons(clientSession.getClientId())) {
            if (coupon.getId() == couponId) {
                succeed = false;
                break;
            }
        }
        return succeed ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.noContent().build();
    }

    @PatchMapping("/companies/coupons-update")
    public ResponseEntity<Coupon> updateCoupon
            (@RequestBody Coupon coupon,
             @RequestParam String token)
            throws CompanyNoExistsException, InvalidUpdateException, CouponNoExistsException, InvalidLoginException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optionalCoupon;
        optionalCoupon = service.updateCoupon(coupon, clientSession.getClientId());

        return optionalCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/companies/coupons-category")
    public ResponseEntity<List<Coupon>> findCompanyCouponsByCategory
            (@RequestParam int category,
             @RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsByCategory =
                service.findCompanyCouponsByCategory(clientSession.getClientId(), category);
        if (companyCouponsByCategory != null && !companyCouponsByCategory.isEmpty()) {
            return ResponseEntity.ok(companyCouponsByCategory);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/coupons-price")
    public ResponseEntity<List<Coupon>> findCompanyCouponsLessThan
            (@RequestParam double price,
             @RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsLessThan = service.findCompanyCouponsLessThan(clientSession.getClientId(), price);
        if (companyCouponsLessThan != null && !companyCouponsLessThan.isEmpty()) {
            return ResponseEntity.ok(companyCouponsLessThan);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/coupons-date")
    public ResponseEntity<List<Coupon>> findCompanyCouponsBeforeDate
            (@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
             @RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsBeforeDate = service.findCompanyCouponsBeforeDate(clientSession.getClientId(), date);
        if (companyCouponsBeforeDate != null && !companyCouponsBeforeDate.isEmpty()) {
            return ResponseEntity.ok(companyCouponsBeforeDate);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/companies/edit")
    public ResponseEntity<Company> editCompany
            (@RequestParam String token,
             @RequestBody Company company)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != COMPANY) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Company> optCompany = service.editCompany(company, clientSession.getClientId());

        return optCompany.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
}
