package com.example.myprojectbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "db_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 256)
    private String username;

    @Column(nullable = false, length = 256)
    private String password;

    @Column(nullable = false, length = 256)
    private String email;

    @Column(nullable = false, length = 256)
    private String role;

    @Column(name = "register_time", nullable = false)
    private LocalDateTime registerTime;

    @Column(nullable = false, length = 256)
    private String uuid;

    @Column(nullable = false)
    private boolean enable;

    public AccountEntity(String username, String password, String email, String role, LocalDateTime registerTime, String uuid, boolean enable) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.registerTime = registerTime;
        this.uuid = uuid;
        this.enable = enable;
    }


}
