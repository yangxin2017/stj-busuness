package com.bjd.smartanalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.bjd.smartanalysis.mapper")
@SpringBootApplication
public class SmartanalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartanalysisApplication.class, args);
    }

}
