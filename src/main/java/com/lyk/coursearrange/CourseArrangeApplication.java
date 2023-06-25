package com.lyk.coursearrange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CourseArrangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseArrangeApplication.class, args);
    }

}
