package com.example.myprojectbackend.vo.reponse;

import lombok.Data;
import lombok.Setter;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Setter
public class AuthorizeVO {
    //創建當前JWT使用者的uuid
    String uuid;
    //JWT的不重複ID
    String tokenId;
    String username;
    String email;
    String role;
    String token;
    Date expire;
    String phoneNumber;
    String authorization;
    LocalDateTime loginDate;
    Long id;
    String avatar;

}
