package com.example.myprojectbackend.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class SpringConfiguration {


    //autowire 注入bean實例
    //bean實例 是使用class創建的實例對象
    @Bean("hello1")
    public HelloService hello1(){
        return new HelloService();
    }
    @Bean("hello2")
    public HelloService hello2(){
        return new HelloService();
    }


//    @Service
//    public class UserService{
//        @Autowired
//        private HelloService helloService;
//    }

}
