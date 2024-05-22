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
@RequestMapping("api/picture")
public class UtilPictureController {


    @Value("${avatarImageFilePath}")
    private String avatarImageFilePath;
//    String localPath="E:\\my-project\\my-project-backend\\src\\main\\resources\\static\\image\\userAvatar\\";


    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        String filePath=avatarImageFilePath+fileName;
        byte[] imageData=Files.readAllBytes(new File(filePath).toPath());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);

    }



    @GetMapping("/test-string")
    @ResponseBody
    public String testString() throws IOException {

        return "success test";
    }
}
