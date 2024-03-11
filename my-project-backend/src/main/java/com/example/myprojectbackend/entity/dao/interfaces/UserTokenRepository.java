package com.example.myprojectbackend.entity.dao.interfaces;

import com.example.myprojectbackend.entity.dao.UserToken;
import com.example.myprojectbackend.entity.vo.reponse.AuthorizeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserTokenRepository extends JpaRepository<UserToken,Integer> {
    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "update db_token set flag=0 where uuid = ?1", nativeQuery = true)
    int lockToken(String uuid);



}
