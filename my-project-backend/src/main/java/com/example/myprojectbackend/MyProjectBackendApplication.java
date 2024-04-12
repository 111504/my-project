package com.example.myprojectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class MyProjectBackendApplication {

    public static void main(String[] args) {
        // 设置应用的默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        SpringApplication.run(MyProjectBackendApplication.class, args);
    }

}
