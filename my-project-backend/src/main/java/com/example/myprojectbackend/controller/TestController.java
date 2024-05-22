package com.example.myprojectbackend.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {
    @PreAuthorize("hasRole('user')")
    @GetMapping("/hello")
    public String test(){
        return "Hwlo World!";
    }


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin")
    public String admin(){
        return "Hwlo admin!";
    }

//    @PreAuthorize("hasAnyRole('USER,ADMIN,EDITER')")
//    @GetMapping("/all")
//    public String allAuthority(){
//        return "Hwlo ALL!";
//    }
}
