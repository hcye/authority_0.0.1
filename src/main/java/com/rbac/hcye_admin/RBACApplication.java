package com.rbac.hcye_admin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@EnableCaching
@SpringBootApplication
public class RBACApplication {
    public static void main(String[] args) {
        SpringApplication.run(RBACApplication.class, args);
    }
}
