package com.mmt;

import com.mmt.user.security.servlet.VerifyServlet;
import com.mmt.user.storage.StorageProperties;
import com.mmt.user.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableConfigurationProperties(StorageProperties.class)
public class LearningParadiseApplication {

    public static void main(String[] args) {

        SpringApplication.run(LearningParadiseApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean indexServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new VerifyServlet());
        registration.addUrlMappings("/getVerifyCode");
        return registration;
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
