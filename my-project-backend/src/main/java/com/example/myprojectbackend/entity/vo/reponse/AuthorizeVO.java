package com.example.myprojectbackend.entity.vo.reponse;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    //創建當前JWT使用者的uuid
    String uuid;
    //JWT的不重複ID
    String tokenId;
    String username;
    String role;
    String token;
    Date expire;
}
