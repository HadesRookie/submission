package com.graduation.submission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = {"com.graduation.submission.dao"})
public class SubmissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubmissionApplication.class, args);
    }

}
