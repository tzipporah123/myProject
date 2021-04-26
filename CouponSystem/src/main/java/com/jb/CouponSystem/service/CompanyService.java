package com.jb.CouponSystem.service;

import com.jb.CouponSystem.data.Entity.Company;
import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.Entity.Customer;
import com.jb.CouponSystem.rest.ex.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This interface is used to access the company service.
 */
public interface CompanyService {
    /**
     * This method creates a coupon for a specific company.
     *
     * @param coupon    the new coupon.
     * @param companyId
     * @return an Optional<coupon> of the now coupon.
     */
    Optional<Coupon> createCoupon(Coupon coupon, long companyId)
            throws CompanyNoExistsException, CouponAlreadyExistsException, CouponNoExistsException;

    /**
     * This method creates a coupon for a specific company.
     *
     * @param companyId
     * @param title     of the new coupon.
     * @return an Optional<coupon> of the new coupon.
     */
    Optional<Coupon> createCoupon(long companyId, String title)
            throws CompanyNoExistsException, CouponAlreadyExistsException;

    /**
     * This method delete a coupon of a specific company.
     *
     * @param couponId:  the id of the requested coupon.
     * @param companyId: the id of the company.
     */
    void deleteCoupon(long couponId, long companyId)
            throws CompanyNoExistsException, CouponNoExistsException;

    /**
     * This method finds all the coupons of a specific company.
     *
     * @param id of the specific company.
     * @return a list of the company coupons.
     */
    List<Coupon> getCompanyCoupons(long id)
            throws CompanyNoExistsException;

    /**
     * This method update a specific coupon of a specific company.
     *
     * @param coupon    with the updated information.
     * @param companyId
     * @return an Optional<coupon> of the updated coupon.
     */
    Optional<Coupon> updateCoupon(Coupon coupon, long companyId)
            throws CompanyNoExistsException, InvalidLoginException,
            CouponNoExistsException, InvalidUpdateException;

    /**
     * This method finds the customer according to his id.
     *
     * @param id of the company.
     * @return an Optional<Company> of the requested company.
     */
    Optional<Company> getCompanyById(long id);

    /**
     * This method finds all the coupons of a specific company of a specific category.
     *
     * @param id       of the specific company.
     * @param category of the coupons.
     * @return a list of the company coupons by category.
     */
    List<Coupon> findCompanyCouponsByCategory(long id, int category)
            throws CompanyNoExistsException;

    /**
     * This method finds all the coupons of a specific company with a price less than a given price.
     *
     * @param id    of the specific company.
     * @param price maximal.
     * @return a list of the company coupons less than price.
     */
    List<Coupon> findCompanyCouponsLessThan(long id, double price)
            throws CompanyNoExistsException;

    /**
     * This method finds all the coupons of a specific company with an expiration date before a given date.
     *
     * @param id   of the specific company.
     * @param date of the latest wanted date.
     * @return list of the company coupons before the date.
     */
    List<Coupon> findCompanyCouponsBeforeDate(long id, LocalDate date)
            throws CompanyNoExistsException;

    /**
     * This method finds the company according to her email and password.
     *
     * @param email    of the company.
     * @param password of the company.
     * @return an Optional<Company> of the requested company.
     */
    Optional<Company> findByEmailAndPassword(String email, String password);

    /**
     * This method determines if a coupon belongs to a specific company.
     *
     * @param companyId
     * @param title     of the coupon.
     * @return true if the coupon does belong to the company else return false.
     */
    boolean belongToCompanyByTitle(long companyId, String title)
            throws CompanyNoExistsException;

    /**
     * This method update a specific company according to his id.
     *
     * @param newCompany with the updated information.
     * @param id         of the company.
     * @return an Optional<Company> of the updated company.
     */
    Optional<Company> editCompany(Company newCompany, long id)
            throws CompanyNoExistsException;

}

