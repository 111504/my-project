package com.example.myprojectbackend.vo.reponse;

import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
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
