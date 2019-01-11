package com.triz.goldenideabox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.triz.goldenideabox.dao")
@EnableTransactionManagement
@EnableAsync
public class GoldenideaboxApplication {

    public static void main(String[] args) {

        SpringApplication.run(GoldenideaboxApplication.class, args);
    }
}
