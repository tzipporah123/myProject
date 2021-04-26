package com.jb.CouponSystem.service;

import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.Entity.Customer;
import com.jb.CouponSystem.rest.ex.CouponAlreadyExistsException;
import com.jb.CouponSystem.rest.ex.CouponNoExistsException;
import com.jb.CouponSystem.rest.ex.CustomerNoExistsException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This interface is used to access the customer service.
 */
public interface CustomerService {
    /**
     * This method finds the customer according to his email and password.
     *
     * @param email    of the customer.
     * @param password of the customer.
     * @return an Optional<Customer> of the requested customer.
     */
    Optional<Customer> findByEmailAndPassword(String email, String password);

    /**
     * This method finds the customer according to his id.
     *
     * @param id of the customer.
     * @return an Optional<Customer> of the requested customer.
     */
    Optional<Customer> getCustomerById(long id);

    /**
     * This method update a specific customer according to his id.
     *
     * @param newCustomer with the updated information.
     * @param id          of the customer.
     * @return an Optional<Customer> of the updated customer.
     */
    Optional<Customer> editCustomer(Customer newCustomer, long id)
            throws CustomerNoExistsException;

    /**
     * This method enables a customer to purchases a coupon.
     *
     * @param couponId:   the id of the requested coupon.
     * @param customerId: the id of the customer.
     * @return an Optional<Coupon> of the coupon the was purchases.
     */
    Optional<Coupon> purchaseCoupon(long couponId, long customerId)
            throws CouponAlreadyExistsException, CouponNoExistsException, CustomerNoExistsException;

    /**
     * This method finds all the coupons that a specific customer does not have.
     *
     * @param id of the specific customer.
     * @return a list of the coupons that the customer does not have.
     */
    List<Coupon> findMissingCustomerCoupons(long id)
            throws CustomerNoExistsException;

    /**
     * This method finds all the coupons of a specific customer.
     *
     * @param id of the specific customer.
     * @return a list of the customer coupons.
     */
    List<Coupon> getCustomerCoupons(long id)
            throws CustomerNoExistsException;

    /**
     * This method finds all the coupons of a specific customer of a specific category.
     *
     * @param id       of the specific customer.
     * @param category of the coupons.
     * @return a list of the customer coupons by category.
     */
    List<Coupon> findCustomerCouponsByCategory(long id, int category)
            throws CustomerNoExistsException;

    /**
     * This method finds all the coupons of a specific customer with a price less than a given price.
     *
     * @param id    of the specific customer.
     * @param price maximal.
     * @return list of the customer coupons less than price.
     */
    List<Coupon> findCustomerCouponsLessThan(long id, double price)
            throws CustomerNoExistsException;

    /**
     * This method finds all the coupons of a specific customer with an expiration date before a given date.
     *
     * @param id   of the specific customer.
     * @param date of the latest wanted date.
     * @return list of the customer coupons before the date.
     */
    List<Coupon> findCustomerCouponsBeforeDate(long id, LocalDate date)
            throws CustomerNoExistsException;
}
