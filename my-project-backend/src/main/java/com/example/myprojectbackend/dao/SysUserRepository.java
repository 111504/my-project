package com.example.myprojectbackend.dao;

import com.example.myprojectbackend.entity.AccountEntity;
import com.example.myprojectbackend.entity.system.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUserEntity,Integer> {

    @Query(value = "select status from sys_user where  email=?1 or username=?1",nativeQuery = true)
    Optional<Integer> checkAccountEnabled(String text);


    @Query(value = "select * from sys_user where username=?1 or email=?1 and status=0",nativeQuery = true)
    SysUserEntity findAccountByNameOrEmail(String nameOrEmail);


    @Query(value = "select id from sys_user where uuid=?1",nativeQuery = true)
    Long findUserIdByUuid(String userUuid);


    @Query(value = "select id from sys_user where username=?1",nativeQuery = true)
    Long findUserIdByUsername(String username);

    @Query(value = "select * from sys_user where username=?1 or email=?1",nativeQuery = true)
    SysUserEntity findByUsernameExist(String username);

    @Transactional
    @Modifying
    @Query(value ="update sys_user set password=?1 where email=?2",nativeQuery = true)
    int updatePasswordByEmail(String password, String email);


    @Query(value = "SELECT sys_user.*, CASE WHEN sys_user.status = '0' THEN 'fail, account active and duplicate' WHEN sys_user.status = '1' THEN 'fail, account disabled' END as match_type FROM sys_user WHERE username = :usernameOrEmail OR email = :usernameOrEmail", nativeQuery = true)
    List<Map<String, Object>> findByUsernameOrEmailWithMatchType(String usernameOrEmail);
}

