package com.jb.CouponSystem.data.rep;

import com.jb.CouponSystem.data.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This interface is used to define access for the customer data.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * This method finds the customer according to his email and password.
     *
     * @param email    of the customer.
     * @param password of the customer.
     * @return a customer if existing.
     */
    Optional<Customer> findByEmailAndPassword(String email, String password);

    /**
     * This method update a given customer.
     *
     * @param id       of the  customer.
     * @param name     of the  customer.
     * @param email    of the  customer.
     * @param password of the  customer.
     */
    @Transactional
    @Modifying
    @Query(value = "update Customer c set c.name =:name, c.email =:email, " +
            "c.password=:password where c.id =:id", nativeQuery = true)
    void updateCustomer(String name,
                        String email,
                        String password,
                        long id);
}

