package com.starter;


import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan({"com"})
@EnableJpaRepositories("com.repositories")
@SpringBootApplication
@EntityScan("com.entities")
public class ExpoPromoterBoot extends SpringBootServletInitializer {



    public static void main(String[] args) throws Exception {
        SpringApplication.run(ExpoPromoterBoot.class, args);
    }

}