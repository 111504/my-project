package com.example.myprojectbackend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

/*
@Data：添加所有欄位的 getter、setter、equals、hashCode、toString 方法。
@NoArgsConstructor：添加一個沒有參數的建構式。
@AllArgsConstructor：添加一個包含所有欄位的建構式

 */


@Entity
@Table(name = "db_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", nullable = false, length = 255)
    private String uuid;

    @Column(name = "token_id", length = 255)
    private String tokenId;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "expire_time")
    private Date expireTime;
    public UserTokenEntity(String uuid, UUID tokenId, int i) {
        this.uuid = uuid;
        this.tokenId = tokenId.toString();
        this.flag = i == 1;
    }

    public UserTokenEntity(String uuid, UUID tokenId, int i, Date date) {
        this.uuid = uuid;
        this.tokenId = tokenId.toString();
        this.flag = i == 1;
        this.expireTime = date;
    }
}
