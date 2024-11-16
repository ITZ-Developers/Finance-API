package com.finance.service.feign;

import com.finance.config.CustomFeignConfig;
import com.finance.form.account.LoginAccountForm;
import com.finance.dto.account.LoginAuthDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name = "account-svr", url = "${auth.internal.auth.url}", configuration = CustomFeignConfig.class)
public interface FeignAccountAuthService {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping(value = "/api/token")
    LoginAuthDto authLogin(@RequestHeader(AUTHORIZATION_HEADER) String authorization, @RequestBody LoginAccountForm loginAccountForm);
}
