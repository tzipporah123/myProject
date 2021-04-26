package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.data.Entity.Company;
import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.Entity.Customer;
import com.jb.CouponSystem.rest.ClientSession;
import com.jb.CouponSystem.rest.ex.*;
import com.jb.CouponSystem.service.CompanyService;
import com.jb.CouponSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jb.CouponSystem.rest.controller.LoginController.ADMIN;

@RestController
@RequestMapping("/api")
public class AdminSystemController {
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public AdminSystemController
            (CustomerService customerService, CompanyService companyService, Map<String, ClientSession> tokensMap) {
        this.customerService = customerService;
        this.companyService = companyService;
        this.tokensMap = tokensMap;
    }

    @GetMapping("/admin/companies")
    public ResponseEntity<Company> getCompany(@RequestParam long companyId,
                                              @RequestParam String token) {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Company> optCompany = companyService.getCompanyById(companyId);

        return optCompany.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/companies/coupons")
    public ResponseEntity<List<Coupon>> getCompanyCoupons(@RequestParam long companyId,
                                                          @RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> allCoupons;
        allCoupons = companyService.getCompanyCoupons(companyId);
        if (allCoupons != null && !allCoupons.isEmpty()) {
            return ResponseEntity.ok(allCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/companies/coupons-new-full")
    public ResponseEntity<Coupon> createNewCoupon
            (@RequestBody Coupon coupon,
             @RequestParam String token)
            throws CompanyNoExistsException, CouponAlreadyExistsException, CouponNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optionalCoupon;
        optionalCoupon = companyService.createCoupon(coupon, coupon.getCompany().getId());

        return optionalCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/admin/companies/coupons-new-empty")
    public ResponseEntity<Coupon> createNewCoupon
            (@RequestParam String title,
             @RequestParam long companyId,
             @RequestParam String token)
            throws CompanyNoExistsException, CouponAlreadyExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optionalCoupon = companyService.createCoupon(companyId, title);

        return optionalCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/admin/companies/delete-coupon")
    public ResponseEntity deleteCoupon
            (@RequestParam long couponId,
             @RequestParam long companyId,
             @RequestParam String token)
            throws CompanyNoExistsException, CouponNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        companyService.deleteCoupon(couponId, companyId);
        boolean succeed = true;
        for (Coupon coupon : companyService.getCompanyCoupons(companyId)) {
            if (coupon.getId() == couponId) {
                succeed = false;
                break;
            }
        }
        return succeed ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/companies/coupons-update")
    public ResponseEntity<Coupon> updateCoupon
            (@RequestBody Coupon coupon,
             @RequestParam long companyId,
             @RequestParam String token)
            throws CompanyNoExistsException, CouponNoExistsException, InvalidUpdateException, InvalidLoginException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optionalCoupon = companyService.updateCoupon(coupon, companyId);

        return optionalCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/companies/coupons-category")
    public ResponseEntity<List<Coupon>> findCompanyCouponsByCategory
            (@RequestParam int category,
             @RequestParam long companyId,
             @RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsByCategory = companyService.findCompanyCouponsByCategory(companyId, category);
        if (companyCouponsByCategory != null && !companyCouponsByCategory.isEmpty()) {
            return ResponseEntity.ok(companyCouponsByCategory);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/companies/coupons-price")
    public ResponseEntity<List<Coupon>> findCompanyCouponsLessThan
            (@RequestParam double price,
             @RequestParam long companyId,
             @RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsLessThan = companyService.findCompanyCouponsLessThan(companyId, price);
        if (companyCouponsLessThan != null && !companyCouponsLessThan.isEmpty()) {
            return ResponseEntity.ok(companyCouponsLessThan);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/companies/coupons-date")
    public ResponseEntity<List<Coupon>> findCompanyCouponsBeforeDate
            (@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
             @RequestParam long companyId,
             @RequestParam String token)
            throws CompanyNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsBeforeDate = companyService.findCompanyCouponsBeforeDate(companyId, date);
        if (companyCouponsBeforeDate != null && !companyCouponsBeforeDate.isEmpty()) {
            return ResponseEntity.ok(companyCouponsBeforeDate);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/customers")
    public ResponseEntity<Customer> getCustomer(@RequestParam String token,
                                                @RequestParam long customerId) {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Customer> optCustomer = customerService.getCustomerById(customerId);

        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/customers/coupons")
    public ResponseEntity<List<Coupon>> getCustomerCoupons(@RequestParam String token,
                                                           @RequestParam long customerId)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> allCoupons;
        allCoupons = customerService.getCustomerCoupons(customerId);
        if (allCoupons != null && !allCoupons.isEmpty()) {
            return ResponseEntity.ok(allCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/customer/purchase-coupon")
    public ResponseEntity<Coupon> addCustomerCoupon(@RequestParam long couponId,
                                                    @RequestParam long customerId,
                                                    @RequestParam String token)
            throws CouponAlreadyExistsException, CouponNoExistsException, CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        Optional<Coupon> optCoupon;
        optCoupon = customerService.purchaseCoupon(couponId, customerId);

        return optCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/customers/coupons-missing")
    public ResponseEntity<List<Coupon>> getCustomerCouponsMissing(@RequestParam String token,
                                                                  @RequestParam long customerId)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> missingCoupons;
        missingCoupons = customerService.findMissingCustomerCoupons(customerId);
        if (missingCoupons != null && !missingCoupons.isEmpty()) {
            return ResponseEntity.ok(missingCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/customers/coupons-category")
    public ResponseEntity<List<Coupon>> findCustomerCouponsByCategory
            (@RequestParam int category,
             @RequestParam long customerId,
             @RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> customerCouponsByCategory = customerService.findCustomerCouponsByCategory(customerId, category);
        if (customerCouponsByCategory != null && !customerCouponsByCategory.isEmpty()) {
            return ResponseEntity.ok(customerCouponsByCategory);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/customers/coupons-price")
    public ResponseEntity<List<Coupon>> findCustomerCouponsLessThan
            (@RequestParam double price,
             @RequestParam long customerId,
             @RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> companyCouponsLessThan = customerService.findCustomerCouponsLessThan(customerId, price);
        if (companyCouponsLessThan != null && !companyCouponsLessThan.isEmpty()) {
            return ResponseEntity.ok(companyCouponsLessThan);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/customers/coupons-date")
    public ResponseEntity<List<Coupon>> findCustomerCouponsBeforeDate
            (@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
             @RequestParam long customerId,
             @RequestParam String token)
            throws CustomerNoExistsException {
        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null || clientSession.getLoginType() != ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        clientSession.access();
        List<Coupon> customerCouponsBeforeDate = customerService.findCustomerCouponsBeforeDate(customerId, date);
        if (customerCouponsBeforeDate != null && !customerCouponsBeforeDate.isEmpty()) {
            return ResponseEntity.ok(customerCouponsBeforeDate);
        }
        return ResponseEntity.noContent().build();
    }
}
