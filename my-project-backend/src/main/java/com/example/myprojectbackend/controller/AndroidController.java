package com.example.myprojectbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
@RestController
@RequestMapping("api/android")
public class AndroidController {
    @GetMapping("/test-string")
    @ResponseBody
    public String testString() throws IOException {

        return "success test";
    }
}




