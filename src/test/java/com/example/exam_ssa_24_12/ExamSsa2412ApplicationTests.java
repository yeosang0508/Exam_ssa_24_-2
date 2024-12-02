package com.example.exam_ssa_24_12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // OpenFeign 활성화
public class ExamSsa2412ApplicationTests {
    public static void main(String[] args) {
        SpringApplication.run(ExamSsa2412ApplicationTests.class, args);
    }
}
