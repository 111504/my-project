package com.example.myprojectbackend;

import com.example.myprojectbackend.config.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.TimeZone;

@SpringBootApplication
@PropertySource("classpath:env.properties")
public class MyProjectBackendApplication {



    public static void main(String[] args) {
        // 设置应用的默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        SpringApplication.run(MyProjectBackendApplication.class, args);




    }

}
