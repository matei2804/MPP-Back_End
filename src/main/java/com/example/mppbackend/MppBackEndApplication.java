package com.example.mppbackend;

import com.example.mppbackend.service.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {"com.example.mppbackend"})
public class MppBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(MppBackEndApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
