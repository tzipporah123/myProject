package com.jb.CouponSystem.data.rep;

import com.jb.CouponSystem.data.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This interface is used to define access to the coupon data.
 */

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    /**
     * This method finds all the coupons of a specific company of a specific category.
     *
     * @param id       of the specific company.
     * @param category of the coupons.
     * @return a list of the company coupons by category.
     */
    List<Coupon> findAllCouponsByCompanyIdAndCategory(long id, int category);

    /**
     * This method finds all the coupons of a specific company with a price less than a given price.
     *
     * @param id    of the specific company.
     * @param price maximal.
     * @return list of the company coupons less than price.
     */
    List<Coupon> findAllCouponsByCompanyIdAndPriceLessThan(long id, double price);

    /**
     * This method finds all the coupons of a specific company with an expiration date before a given date.
     *
     * @param id   of the specific company.
     * @param date of the latest wanted date.
     * @return list of the company coupons before the date.
     */
    List<Coupon> findAllCouponsByCompanyIdAndEndDateLessThan(long id, LocalDate date);

    /**
     * This method finds all the coupons of a specific customer of a specific category.
     *
     * @param id       of the specific customer.
     * @param category of the coupons.
     * @return a list of the customer coupons by category.
     */
    List<Coupon> findAllCouponsByCustomersIdAndCategory(long id, int category);

    /**
     * This method finds all the coupons of a specific customer with a price less than a given price.
     *
     * @param id    of the specific customer.
     * @param price maximal.
     * @return list of the customer coupons less than price.
     */
    List<Coupon> findAllCouponsByCustomersIdAndPriceLessThan(long id, double price);

    /**
     * This method finds all the coupons of a specific customer with an expiration date before a given date.
     *
     * @param id   of the specific customer.
     * @param date of the latest wanted date.
     * @return list of the customer coupons before the date.
     */
    List<Coupon> findAllCouponsByCustomersAndEndDateLessThan(long id, LocalDate date);

    /**
     * This method finds all the coupons in the system with an expiration date that already past.
     *
     * @return the list of expired coupons
     */

    @Query("select c from Coupon c where c.endDate > CURRENT_DATE")
    List<Coupon> findExpiredCoupons();

    /**
     * This method finds a coupon according to his id.
     *
     * @param Id: the id of the requested coupon.
     * @return the requested coupon if existent.
     */
    Optional<Coupon> findCouponById(long Id);

    /**
     * This method update a given coupon.
     *
     * @param id          of the coupon.
     * @param startDate   of the coupon.
     * @param endDate     of the coupon.
     * @param amount      of the coupon.
     * @param category    of the coupon.
     * @param description of the coupon.
     * @param price       of the coupon.
     * @param imageURL    of the coupon.
     */
    @Transactional
    @Modifying
    @Query(value = "update Coupon c set c.start_Date =:startDate, c.end_Date =:endDate, " +
            " c.amount =:amount, c.category =:category, c.description =:description, c.price =:price, " +
            " c.imageURL =:imageURL where c.id =:id", nativeQuery = true)
    void updateCoupon(LocalDate startDate,
                      LocalDate endDate,
                      int amount,
                      int category,
                      String description,
                      double price,
                      String imageURL,
                      long id);

    /**
     * This method update a given coupon.
     *
     * @param soldAmount of the coupon.
     * @param id         of the coupon.
     */
    @Transactional
    @Modifying
    @Query(value = "update Coupon c set c.sold_amount =:soldAmount where c.id =:id", nativeQuery = true)
    void updateCouponSoldAmount(int soldAmount, long id);
}
