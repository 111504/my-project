package com.example.myprojectbackend.dao;

import com.example.myprojectbackend.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Integer> {
    @Query(value = "select * from db_account where  username=?1 or email=?1",nativeQuery = true)
    AccountEntity findByUsernameOrEmail(String text);
    AccountEntity findByUsername(String username);
    @Query(value = "select enable from db_account where  email=?1 or username=?1",nativeQuery = true)
    Optional<Integer> checkAccountEnabled(String text);


    @Transactional
    @Modifying
    @Query(value ="update db_account set username=?1 ,password=?2 , role=?3 ,register_time=?4,uuid=?5,enable=?6 where email=?7",nativeQuery = true)
    int enableAccount(String username, String password, String role, LocalDateTime registertime,String uuid,boolean enable,String email);



    @Transactional
    @Modifying
    @Query(value ="update db_account set enable=0 where email=?1",nativeQuery = true)
    int disableAccount(String email);


    @Transactional
    @Modifying
    @Query(value ="update db_account set password=?1 where email=?2",nativeQuery = true)
    int updatePasswordByEmail(String password, String email);

    @Query(value = "select * from db_account where username=?1 or email=?1 and enable=1",nativeQuery = true)
    AccountEntity findAccountByNameOrEmail(String nameOrEmail);





}
