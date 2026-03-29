package com.companya.labms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LabmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabmsApplication.class, args);
    }
}