package com.finance.service;

import com.finance.constant.FinanceConstant;
import com.finance.dto.account.KeyWrapperDto;
import com.finance.dto.account.SubKeyWrapperDto;
import com.finance.jwt.FinanceJwt;
import com.finance.model.Account;
import com.finance.repository.AccountRepository;
import com.finance.service.impl.UserServiceImpl;
import com.finance.utils.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

@Service
public class KeyService {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AccountRepository accountRepository;
    private final ConcurrentMap<String, String> concurrentMap;

    public KeyService(@Qualifier("applicationConfig") ConcurrentMap<String, String> concurrentMap) {
        this.concurrentMap = concurrentMap;
    }

    public long getCurrentUser(){
        FinanceJwt financeJwt = userService.getAddInfoFromToken();
        return financeJwt.getAccountId();
    }

    public String getFinanceSecretKey() {
        return concurrentMap.get(FinanceConstant.FINANCE_SECRET_KEY);
    }

    public String getKeyInformationSecretKey() {
        return concurrentMap.get(FinanceConstant.KEY_INFORMATION_SECRET_KEY);
    }

    public String getDecryptPasswordSecretKey() {
        return concurrentMap.get(FinanceConstant.DECRYPT_PASSWORD_SECRET_KEY);
    }

    public String getUserSecretKey(){
        Account account = accountRepository.findById(getCurrentUser()).orElse(null);
        if (account != null){
            return AESUtils.decrypt(getKeyInformationSecretKey(), account.getSecretKey(), FinanceConstant.AES_ZIP_ENABLE);
        }
        return null;
    }

    public String getUserPublicKey(){
        Account account = accountRepository.findById(getCurrentUser()).orElse(null);
        if (account != null){
            return account.getPublicKey();
        }
        return null;
    }

    public void clearConcurrentMap(){
        concurrentMap.clear();
    }

    public KeyWrapperDto getFinanceKeyWrapper() {
        return new KeyWrapperDto(getFinanceSecretKey(), getUserSecretKey());
    }

    public KeyWrapperDto getKeyInformationKeyWrapper() {
        return new KeyWrapperDto(getKeyInformationSecretKey(), getUserSecretKey());
    }

    public SubKeyWrapperDto getFinanceSubKeyWrapper() {
        return new SubKeyWrapperDto(getFinanceSecretKey(), getUserSecretKey());
    }

    public SubKeyWrapperDto getKeyInformationSubKeyWrapper() {
        return new SubKeyWrapperDto(getKeyInformationSecretKey(), getUserSecretKey());
    }
}
