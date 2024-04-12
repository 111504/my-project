package com.example.myprojectbackend.entity.system;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "avatar", length = 255, columnDefinition = "varchar(255) default '1.png'")
    private String avatar = "1.png";

    @Column(name = "email", length = 100, columnDefinition = "varchar(100) default ''")
    private String email = "";

    @Column(name = "phonenumber", length = 11, columnDefinition = "varchar(11) default ''")
    private String phoneNumber = "";

    @Column(name = "login_date")
    private LocalDateTime loginDate;

    @Column(name = "status", length = 1, columnDefinition = "char(1) default '0'")
    private String status = "0";

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "uuid", length = 256)
    private String uuid;


    // Getters and Setters
    // Note: Implement getters and setters here for all the fields.
public SysUserEntity(String username, String password, String avatar, String email, String phoneNumber, String status, LocalDateTime createTime, LocalDateTime updateTime, String remark,String uuid){
    this.username=username;
    this.password=password;
    this.avatar=avatar;
    this.email=email;
    this.phoneNumber=phoneNumber;
    this.createTime=createTime;
    this.updateTime=updateTime;
    this.remark=remark;
    this.uuid=uuid;

}

    public SysUserEntity(String username, String password, String email, String phonenumber, LocalDateTime creatTime, String remark, String uuid) {
    this.username=username;
    this.password=password;
    this.email=email;
    this.phoneNumber=phonenumber;
    this.createTime=creatTime;
    this.remark=remark;
    this.uuid=uuid;
}
}
