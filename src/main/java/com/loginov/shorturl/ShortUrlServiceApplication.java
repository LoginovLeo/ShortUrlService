package com.loginov.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.loginov.shorturl.repository")
public class ShortUrlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlServiceApplication.class, args);
    }

//    @Bean
//    public FilterRegistrationBean<FilterUrlGenerate> someFilterRegistration() {
//
//        FilterRegistrationBean<FilterUrlGenerate> registration = new FilterRegistrationBean();
//        registration.setFilter(new FilterUrlGenerate());
//        registration.addUrlPatterns("/url/generate");
//        return registration;
//    }
}
