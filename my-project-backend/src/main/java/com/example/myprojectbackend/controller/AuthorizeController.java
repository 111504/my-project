package com.example.myprojectbackend.controller;

import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.dao.UserTokenRepository;
import com.example.myprojectbackend.service.SysUserService;
import com.example.myprojectbackend.vo.request.ConfirmResetVO;
import com.example.myprojectbackend.vo.request.EmailRegisterVO;
import com.example.myprojectbackend.vo.request.EmailResetVO;
import com.example.myprojectbackend.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("api/auth")
public class AuthorizeController {

    @Resource
    AccountService accountService;

    @Autowired
    UserTokenRepository userTokenRepository;

@Resource
    SysUserService sysUserService;

    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)")
                                        String type, HttpServletRequest request){
        return this.messageHandle(()->
                accountService.registerEmailVerifyCode(type,email,request.getRemoteAddr()));

    }
    //註冊
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo){
        return this.messageHandle(()->accountService.registerEmailAccount(vo));

    }
    //驗證驗證碼
    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetVO vo){
        return this.messageHandle(()->accountService.resetConfirm(vo));
    }

    //重置密碼
    @PostMapping("/reset-password")
    public RestBean<Void> resetPassword(@RequestBody @Valid EmailResetVO vo){
        return this.messageHandle(()->accountService.resetEmailAccountPassword(vo));
    }


    @GetMapping("/validateToken")
    public String validateToken()
    {


        return RestBean.success("TOKEN_SUCCESS","Token authority success").asJsonString();
    }



    @GetMapping("/testUser")
    public String testUser()
    {


        return sysUserService.getUserAuthorityInfo(2L);
    }



    private <T> RestBean<T> messageHandle(Supplier<String> action){
        String message=action.get();

        if(Objects.equals(message, "SUCCESS"))
            return RestBean.success();
        else
            return RestBean.failure(400,message);
    }
}
