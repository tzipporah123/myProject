package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.Entity.Customer;
import com.jb.CouponSystem.rest.ClientSession;
import com.jb.CouponSystem.rest.ex.CouponAlreadyExistsException;
import com.jb.CouponSystem.rest.ex.CouponNoExistsException;
import com.jb.CouponSystem.rest.ex.CustomerNoExistsException;
import com.jb.CouponSystem.service.CustomerService;
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

import static com.jb.CouponSystem.rest.controller.LoginController.CUSTOMER;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerSystemController {
    private final CustomerService service;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public CustomerSystemController(CustomerService service, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.service = service;
        this.tokensMap = tokensMap;
    }

    @GetMapping("/customers")
    public ResponseEntity<Customer> getCustomer(@RequestParam String token) {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Customer> optCustomer = service.getCustomerById(clientSession.getClientId());
        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PatchMapping("/customers/edit")
    public ResponseEntity<Customer> editCustomer
            (@RequestParam String token,
             @RequestBody Customer customer)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Customer> optCustomer = service.editCustomer(customer, clientSession.getClientId());

        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/customers/coupons")
    public ResponseEntity<List<Coupon>> getCustomerCoupons(@RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> allCoupons;
        allCoupons = service.getCustomerCoupons(clientSession.getClientId());
        if (allCoupons != null && !allCoupons.isEmpty()) {
            return ResponseEntity.ok(allCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/customer/purchase-coupon")
    public ResponseEntity<Coupon> purchaseCustomerCoupon(@RequestParam long couponId,
                                                         @RequestParam String token)
            throws CouponAlreadyExistsException, CouponNoExistsException, CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optCoupon;
        optCoupon = service.purchaseCoupon(couponId, clientSession.getClientId());

        return optCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/customers/coupons-missing")
    public ResponseEntity<List<Coupon>> getCustomerCouponsMissing(@RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> missingCoupons;
        missingCoupons = service.findMissingCustomerCoupons(clientSession.getClientId());
        if (missingCoupons != null && !missingCoupons.isEmpty()) {
            return ResponseEntity.ok(missingCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/coupons-category")
    public ResponseEntity<List<Coupon>> findCustomerCouponsByCategory
            (@RequestParam int category,
             @RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> customerCouponsByCategory =
                service.findCustomerCouponsByCategory(clientSession.getClientId(), category);
        if (customerCouponsByCategory != null && !customerCouponsByCategory.isEmpty()) {
            return ResponseEntity.ok(customerCouponsByCategory);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/coupons-price")
    public ResponseEntity<List<Coupon>> findCustomerCouponsLessThan
            (@RequestParam double price,
             @RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsLessThan = service.findCustomerCouponsLessThan(clientSession.getClientId(), price);
        if (companyCouponsLessThan != null && !companyCouponsLessThan.isEmpty()) {
            return ResponseEntity.ok(companyCouponsLessThan);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/coupons-date")
    public ResponseEntity<List<Coupon>> findCompanyCouponsBeforeDate
            (@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
             @RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != CUSTOMER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> customerCouponsBeforeDate = service.findCustomerCouponsBeforeDate(clientSession.getClientId(), date);
        if (customerCouponsBeforeDate != null && !customerCouponsBeforeDate.isEmpty()) {
            return ResponseEntity.ok(customerCouponsBeforeDate);
        }
        return ResponseEntity.noContent().build();
    }
}