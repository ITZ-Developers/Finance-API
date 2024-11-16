package com.finance.component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import com.finance.service.impl.UserServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
public class OAuth2FeignRequestInterceptor implements RequestInterceptor { // cấu hình cho các cuộc gọi đến các service khác

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private static final String BASIC_AUTH_TYPE = "Basic";

    private String internalAuth;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public void apply(RequestTemplate template) {
//        if(template.headers().containsKey(FeignAccountAuthService.LOGIN_TYPE)){
//            if(Objects.equals(template.headers().get(FeignAccountAuthService.LOGIN_TYPE).toArray()[0], FeignConst.LOGIN_TYPE_INTERNAL)){
//                String auth = template.headers().get(AUTHORIZATION_HEADER).toArray()[0].toString();
//                log.error("-----------> internal = " + auth);
//                template.header(AUTHORIZATION_HEADER, auth);
//            }else{
//                log.error("-----------> not found type = "+template.headers().get(FeignAccountAuthService.LOGIN_TYPE).toArray()[0]);
//            }
//            template.removeHeader(FeignAccountAuthService.LOGIN_TYPE);
//        }else{
//            log.error("-----------> Constructing Header {} for Token {}, token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE, String.format("%s %s", BEARER_TOKEN_TYPE, userService.AUTH_SERVER_TOKEN));
//            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, userService.AUTH_SERVER_TOKEN));
//        }
    }
}
