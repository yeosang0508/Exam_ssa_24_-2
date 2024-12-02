package com.example.exam_ssa_24_12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ExamSsa2412Application {
    public static void main(String[] args) {
        SpringApplication.run(ExamSsa2412Application.class, args);
    }
}

