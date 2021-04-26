package com.jb.CouponSystem.rest.controller;

import com.jb.CouponSystem.rest.AdminSystem;
import com.jb.CouponSystem.rest.CompanySystem;
import com.jb.CouponSystem.rest.ex.InvalidLoginException;
import com.jb.CouponSystem.rest.ClientSession;
import com.jb.CouponSystem.rest.CustomerSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class LoginController {
    public static final int ADMIN = 1;
    public static final int COMPANY = 2;
    public static final int CUSTOMER = 3;

    private final AdminSystem adminSystem;
    private final CompanySystem companySystem;
    private final CustomerSystem customerSystem;
    private Map<String, ClientSession> tokensMap;

    @Autowired
    public LoginController(AdminSystem adminSystem,
                           CompanySystem companySystem,
                           CustomerSystem customerSystem,
                           @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.adminSystem = adminSystem;
        this.companySystem = companySystem;
        this.customerSystem = customerSystem;
        this.tokensMap = tokensMap;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam int loginType)
            throws InvalidLoginException {
        ClientSession session;
        Token token = new Token();
        switch (loginType) {
            case ADMIN:
                session = adminSystem.createSession(email, password);
                tokensMap.put(token.getToken(), session);
                return ResponseEntity.ok(token);
            case COMPANY:
                session = companySystem.createSession(email, password);
                tokensMap.put(token.getToken(), session);
                return ResponseEntity.ok(token);
            case CUSTOMER:
                session = customerSystem.createSession(email, password);
                tokensMap.put(token.getToken(), session);
                return ResponseEntity.ok(token);
            default:
                throw new InvalidLoginException("Login type is not supported!");
        }
    }

    public class Token {
        private static final int LENGTH_TOKEN = 15;
        private String tokenName;

        public Token() {
            tokenName = generateToken();
        }

        private String generateToken() {
            return UUID.randomUUID()
                    .toString()
                    .replaceAll("-", "")
                    .substring(0, LENGTH_TOKEN);
        }

        public String getToken() {
            return tokenName;
        }

        public void setToken(String tokenName) {
            this.tokenName = tokenName;
        }
    }

}
