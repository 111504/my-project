package com.example.myprojectbackend.service;
import com.example.myprojectbackend.vo.request.ConfirmResetVO;
import com.example.myprojectbackend.vo.request.EmailRegisterVO;
import com.example.myprojectbackend.vo.request.EmailResetVO;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends UserDetailsService{


    String registerEmailVerifyCode(String type,String email,String ip);

    String registerEmailAccount(EmailRegisterVO vo);

    String resetConfirm(ConfirmResetVO vo);
    String resetEmailAccountPassword(EmailResetVO vo);

     List<GrantedAuthority> getUserAuthority(Long userId);

}
