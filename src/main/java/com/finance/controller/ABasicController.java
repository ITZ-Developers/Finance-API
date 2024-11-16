package com.finance.controller;

import com.finance.dto.ApiMessageDto;
import com.finance.jwt.FinanceJwt;
import com.finance.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.List;

public class ABasicController {
    @Autowired
    private UserServiceImpl userService;

    public long getCurrentUser(){
        FinanceJwt financeJwt = userService.getAddInfoFromToken();
        return financeJwt.getAccountId();
    }

    public long getTokenId(){
        FinanceJwt financeJwt = userService.getAddInfoFromToken();
        return financeJwt.getTokenId();
    }

    public FinanceJwt getSessionFromToken(){
        return userService.getAddInfoFromToken();
    }

    public boolean isSuperAdmin(){
        FinanceJwt financeJwt = userService.getAddInfoFromToken();
        if(financeJwt !=null){
            return financeJwt.getIsSuperAdmin();
        }
        return false;
    }

    public String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            OAuth2AuthenticationDetails oauthDetails =
                    (OAuth2AuthenticationDetails) authentication.getDetails();
            if (oauthDetails != null) {
                return oauthDetails.getTokenValue();
            }
        }
        return null;
    }

    public <T> ApiMessageDto<T> makeErrorResponse(String code, String message){
        ApiMessageDto<T> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setResult(false);
        apiMessageDto.setCode(code);
        apiMessageDto.setMessage(message);
        return apiMessageDto;
    }

    public <T> ApiMessageDto<T> makeSuccessResponse(T data, String message){
        ApiMessageDto<T> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(data);
        apiMessageDto.setMessage(message);
        return apiMessageDto;
    }

    public boolean hasRole(String permissionCode) {
        List<String> authorities = userService.getAuthorities();
        return authorities != null && authorities.contains(permissionCode);
    }
}
