package com.jb.CouponSystem.service;

import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.Entity.Customer;
import com.jb.CouponSystem.data.rep.CouponRepository;
import com.jb.CouponSystem.data.rep.CustomerRepository;
import com.jb.CouponSystem.rest.ex.CouponAlreadyExistsException;
import com.jb.CouponSystem.rest.ex.CouponNoExistsException;
import com.jb.CouponSystem.rest.ex.CustomerNoExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CouponRepository couponRepo;
    private final CustomerRepository customerRepo;

    @Autowired
    public CustomerServiceImpl(CouponRepository couponRepo,
                               CustomerRepository customerRepo) {
        this.couponRepo = couponRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public Optional<Customer> findByEmailAndPassword(String email, String password) {
        return customerRepo.findByEmailAndPassword(email, password);
    }

    @Override
    public Optional<Customer> getCustomerById(long id) {
        return customerRepo.findById(id);
    }

    @Override
    public Optional<Customer> editCustomer(Customer newCustomer, long id) throws CustomerNoExistsException {
        if (newCustomer != null) {
            customerRepo.updateCustomer(
                    newCustomer.getName(),
                    newCustomer.getEmail(),
                    newCustomer.getPassword(),
                    id);
            return Optional.of(newCustomer);
        }
        return Optional.empty();
    }

    public Customer getCustomer(long id)
            throws CustomerNoExistsException {
        if (customerRepo.findById(id).isPresent()) {
            return customerRepo.findById(id).get();
        }
        throw new CustomerNoExistsException("No customer was fond");
    }

    @Override
    public Optional<Coupon> purchaseCoupon(long couponId, long customerId)
            throws CouponAlreadyExistsException, CouponNoExistsException, CustomerNoExistsException {
        for (Coupon coupon : getCustomerCoupons(customerId)) {
            if (coupon.getId() == couponId) {
                throw new CouponAlreadyExistsException("Sorry, you can not purchase the sane coupon twice.");
            }
        }
        getCustomerCoupons(customerId).add(getCoupon(couponId));
        decrementCouponAmount(couponId);
        increaseCouponsoldAmount(couponId);
        return couponRepo.findCouponById(couponId);
    }

    private void increaseCouponsoldAmount(long couponId) throws CouponNoExistsException {
        Coupon coupon = getCoupon(couponId);
        int couponSoldAmount = coupon.getSoldAmount();
        couponRepo.updateCouponSoldAmount(coupon.getSoldAmount() + 1,couponId);
    }

    private void decrementCouponAmount(long couponId)
            throws CouponNoExistsException {
        Coupon coupon = getCoupon(couponId);
        int couponAmount = coupon.getAmount();
        if (couponAmount <= 0) {
            throw new CouponNoExistsException("Sorry, we are out of the coupons you want to purchase.");
        }
        couponRepo.updateCoupon(coupon.getStartDate(),
                coupon.getEndDate(),
                coupon.getAmount() - 1,
                coupon.getCategory(),
                coupon.getDescription(),
                coupon.getPrice(),
                coupon.getImageURL(),
                coupon.getId());
    }

    private Coupon getCoupon(long couponId)
            throws CouponNoExistsException {
        if (couponRepo.findCouponById(couponId).isPresent()) {
            return couponRepo.findCouponById(couponId).get();
        }
        throw new CouponNoExistsException("Sorry, this coupon does not exists");
    }

    @Override
    public List<Coupon> findMissingCustomerCoupons(long id)
            throws CustomerNoExistsException {
        List<Coupon> missingCoupons = couponRepo.findAll();
        for (Coupon coupon : couponRepo.findAll()) {
            for (Coupon customerCoupon : getCustomerCoupons(id)) {
                if (customerCoupon == coupon) {
                    missingCoupons.remove(coupon);
                }
            }
        }
        return missingCoupons;
    }

    @Override
    public List<Coupon> getCustomerCoupons(long id)
            throws CustomerNoExistsException {
        return getCustomer(id).getCoupons();
    }

    @Override
    public List<Coupon> findCustomerCouponsByCategory(long id, int category) {
        return couponRepo.findAllCouponsByCustomersIdAndCategory(id, category);
    }

    @Override
    public List<Coupon> findCustomerCouponsLessThan(long id, double price) {
        return couponRepo.findAllCouponsByCustomersIdAndPriceLessThan(id, price);
    }

    @Override
    public List<Coupon> findCustomerCouponsBeforeDate(long id, LocalDate date) {
        return couponRepo.findAllCouponsByCustomersAndEndDateLessThan(id, date);
    }
}

