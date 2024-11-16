package com.finance;

import com.finance.component.AuditorAwareImpl;
import com.finance.utils.GenerateUtils;
import com.finance.utils.RSAUtils;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EnableFeignClients
@EnableScheduling
@EnableAspectJAutoProxy
//@EnableEurekaClient
public class Application {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("Spring boot application running in UTC timezone :" + new Date());
    }

    public static void main(String[] args) {
        //initSystemKey();
        SpringApplication.run(Application.class, args);
    }


    public static void initSystemKey(){
        KeyPair keyPair = RSAUtils.generateKeyPair();
        String pub = RSAUtils.keyToString(keyPair.getPublic());
        //pub = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJGgvaCcfXxFfh0D1koJJuzyt4ju7tzfdg85YLng/PTxhzi/kgA/tke4wg54WZCWAFMzvSGsMIahNVakJKpeqrECAwEAAQ==";
        String priv = RSAUtils.keyToString(keyPair.getPrivate());

        String keyService = GenerateUtils.generateRandomString(16);
        keyService = RSAUtils.encrypt(pub,keyService);

        String keyKeyService = GenerateUtils.generateRandomString(16);
        keyKeyService = RSAUtils.encrypt(pub,keyKeyService);

        String keyDecryptPassword = GenerateUtils.generateRandomString(16);
        keyDecryptPassword = RSAUtils.encrypt(pub,keyDecryptPassword);

        System.out.println("pub: "+pub);
        System.out.println("Private: "+priv);
        System.out.println("keyService: "+keyService);
        System.out.println("keyKeyService: "+keyKeyService);
        System.out.println("keyDecryptPassword: "+ keyDecryptPassword);
    }
}