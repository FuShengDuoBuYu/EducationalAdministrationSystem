package com.se.lab2_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Lab2BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab2BackendApplication.class, args);
    }

}
