package com.jb.CouponSystem.data.rep;

import com.jb.CouponSystem.data.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This interface is used to define access for the company data.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * This method finds the company according to her email and password.
     *
     * @param email    of the company.
     * @param password of the company.
     * @return a customer if existing.
     */
    Optional<Company> findByEmailAndPassword(String email, String password);

    /**
     * This method update a given company.
     *
     * @param id       of the  company.
     * @param name     of the company.
     * @param email    of the company.
     * @param password of the  company.
     */
    @Transactional
    @Modifying
    @Query(value = "update Company c set c.name =:name, c.email =:email, " +
            "c.password=:password where c.id =:id", nativeQuery = true)
    void updateCompany(String name,
                        String email,
                        String password,
                        long id);
}

