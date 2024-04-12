package com.example.myprojectbackend.dao;

import com.example.myprojectbackend.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserTokenRepository extends JpaRepository<UserTokenEntity,Integer> {
    @Modifying
    @Transactional
    @Query(value = "update db_token set flag=0 where uuid = ?1", nativeQuery = true)
    int lockToken(String uuid);


    @Modifying
    @Transactional
    @Query(value = "update db_token set flag=0 where  token_id= ?1", nativeQuery = true)
    int lockTokenByTokenId(String tokenId);

//    @Query(value = "select username from db_account where  username=?1", nativeQuery = true)
//    String checkUsernameExists(String username);

    @Query(value = "select flag from db_token where token_id=?1", nativeQuery = true)
    boolean checkTokenFlag(String tokenId);

    @Query(value = "select uuid from db_token where uuid=?1 limit 2", nativeQuery = true)
    List<String> checkUserExist(String uuid);



}
