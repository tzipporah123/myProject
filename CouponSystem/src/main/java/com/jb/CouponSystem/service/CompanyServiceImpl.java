package com.jb.CouponSystem.service;

import com.jb.CouponSystem.data.Entity.Company;
import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.rep.CompanyRepository;
import com.jb.CouponSystem.data.rep.CouponRepository;
import com.jb.CouponSystem.rest.ex.CompanyNoExistsException;
import com.jb.CouponSystem.rest.ex.CouponAlreadyExistsException;
import com.jb.CouponSystem.rest.ex.CouponNoExistsException;
import com.jb.CouponSystem.rest.ex.InvalidUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CouponRepository couponRepo;
    private final CompanyRepository companyRepo;

    @Autowired
    public CompanyServiceImpl(CouponRepository couponRepo,
                              CompanyRepository companyRepo) {
        this.couponRepo = couponRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public Optional<Coupon> createCoupon(Coupon coupon, long companyId)
            throws CompanyNoExistsException, CouponAlreadyExistsException, CouponNoExistsException {
        if (coupon == null) {
            return Optional.empty();
        }
        if (coupon.getCompany().getId() != companyId) {
            throw new CouponNoExistsException("This coupon does not belong to this company!");
        }
        if (companyRepo.findById(companyId).isPresent()) {
            if (belongToCompanyByTitle(companyId, coupon.getTitle())) {
                throw new CouponAlreadyExistsException("This title already exists for an another coupon!");
            }
            coupon.setId(0);
            couponRepo.save(coupon);
            return Optional.of(coupon);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Coupon> createCoupon(long companyId, String title)
            throws CompanyNoExistsException, CouponAlreadyExistsException {
        if (companyRepo.findById(companyId).isPresent()) {
            if (belongToCompanyByTitle(companyId, title)) {
                throw new CouponAlreadyExistsException("This title already exists for an another coupon!");
            }
            Coupon nowCoupon = new Coupon(companyRepo.findById(companyId).get(), title);
            couponRepo.save(nowCoupon);
            return Optional.of(nowCoupon);
        }
        return Optional.empty();
    }

    @Override
    public void deleteCoupon(long couponId, long companyId)
            throws CouponNoExistsException {
        if (couponRepo.findCouponById(couponId).isPresent()) {
            if (couponRepo.findCouponById(couponId).get().getCompany().getId() != companyId) {
                throw new CouponNoExistsException("This coupon does not belong to this company!");
            }
            couponRepo.deleteById(couponId);
        } else {
            throw new CouponNoExistsException("This coupon does not exists!");
        }
    }

    @Override
    public List<Coupon> getCompanyCoupons(long id) throws CompanyNoExistsException {
        if (companyRepo.findById(id).isPresent()) {
            return companyRepo.findById(id).get().getCoupons();
        } else {
            throw new CompanyNoExistsException("There is no company with id: " + id);
        }
    }

    @Override
    public Optional<Coupon> updateCoupon(Coupon coupon, long companyId)
            throws CompanyNoExistsException, CouponNoExistsException, InvalidUpdateException {
        if (coupon != null) {
            if (coupon.getCompany().getId() != companyId) {
                throw new CouponNoExistsException("This coupon does not belong to this company!");
            }
            if (!belongToCompanyByTitle(companyId, coupon.getTitle())) {
                throw new InvalidUpdateException("Can not change the title!");
            }
            couponRepo.updateCoupon(coupon.getStartDate(),
                    coupon.getEndDate(),
                    coupon.getAmount(),
                    coupon.getCategory(),
                    coupon.getDescription(),
                    coupon.getPrice(),
                    coupon.getImageURL(),
                    coupon.getId());
            return Optional.of(coupon);
        }
        return Optional.empty();
    }

    public boolean belongToCompanyByTitle(long companyId, String title)
            throws CompanyNoExistsException {
        for (Coupon companyCoupon : getCompanyCoupons(companyId)) {
            if (companyCoupon.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Company> editCompany(Company newCompany, long id) throws CompanyNoExistsException {
        if (newCompany != null) {
            companyRepo.updateCompany(
                    newCompany.getName(),
                    newCompany.getEmail(),
                    newCompany.getPassword(),
                    id);
            return Optional.of(newCompany);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Company> getCompanyById(long id) {
        return companyRepo.findById(id);
    }

    @Override
    public List<Coupon> findCompanyCouponsByCategory(long id, int category) {
        return couponRepo.findAllCouponsByCompanyIdAndCategory(id, category);
    }

    @Override
    public List<Coupon> findCompanyCouponsLessThan(long id, double price) {
        return couponRepo.findAllCouponsByCompanyIdAndPriceLessThan(id, price);
    }

    @Override
    public List<Coupon> findCompanyCouponsBeforeDate(long id, LocalDate date) {
        return couponRepo.findAllCouponsByCompanyIdAndEndDateLessThan(id, date);
    }

    @Override
    public Optional<Company> findByEmailAndPassword(String email, String password) {
        return companyRepo.findByEmailAndPassword(email, password);
    }
}
