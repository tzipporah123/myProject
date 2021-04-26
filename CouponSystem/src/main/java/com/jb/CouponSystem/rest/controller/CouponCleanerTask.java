package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.data.Entity.Coupon;
import com.jb.CouponSystem.data.rep.CouponRepository;
import com.jb.CouponSystem.rest.ClientSession;
import com.jb.CouponSystem.rest.ex.CompanyNoExistsException;
import com.jb.CouponSystem.rest.ex.CouponNoExistsException;
import com.jb.CouponSystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@Component
public class CouponCleanerTask {
    private final Map<LoginController.Token, ClientSession> tokensMap;
    private final CouponRepository couponRepo;
    private final CompanyService companyService;

    @Autowired
    public CouponCleanerTask(Map<LoginController.Token, ClientSession> tokensMap,
                             CouponRepository couponRepo, CompanyService companyService) {
        this.tokensMap = tokensMap;
        this.couponRepo = couponRepo;
        this.companyService = companyService;
    }

    /**
     * This method remove clientSession and attached token every 10 minute if was not used in the last half hour.
     */
    @Scheduled(cron = "* */10 * * * *", zone = "Asia/Jerusalem")
    public void removeClientSession() {
        if (!tokensMap.isEmpty()) {
            Collection<ClientSession> clientSessions = tokensMap.values();
            long halfHourInMillis = 1_800_000;
            clientSessions.removeIf(cs -> cs.getLastAccessedMillis() + halfHourInMillis < System.currentTimeMillis());
        }
    }

    /**
     * This method deletes all the Expired coupons every day at 1:01.
     */
    @Scheduled(cron = "0 1 1 * * ?", zone = "Asia/Jerusalem")
    public void deleteExpiredCoupons()
            throws CompanyNoExistsException, CouponNoExistsException {
        for (Coupon coupon : couponRepo.findExpiredCoupons()) {
            companyService.deleteCoupon(coupon.getId(), coupon.getCompany().getId());
        }
    }
}
