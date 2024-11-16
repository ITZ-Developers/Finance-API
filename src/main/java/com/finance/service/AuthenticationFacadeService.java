package com.finance.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeService {
    Authentication getAuthentication();
}
