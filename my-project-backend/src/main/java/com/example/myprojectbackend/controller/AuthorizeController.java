package com.example.myprojectbackend.controller;


import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.entity.vo.request.ConfirmResetVO;
import com.example.myprojectbackend.entity.vo.request.EmailRegisterVO;
import com.example.myprojectbackend.entity.vo.request.EmailResetVO;
import com.example.myprojectbackend.service.AccountService;
import jakarta.annotation.Resource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("api/auth")
public class AuthorizeController {

    @Autowired
    AccountService accountService;
    //產生驗整碼
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


    @PostMapping("/test")
    public String importTemplate(@RequestBody String input)
    {
       return "test";
    }



   private <T> RestBean<T> messageHandle(Supplier<String> action){
       String message=action.get();

       if(message==null)
           return RestBean.success();
       else
           return RestBean.failure(400,message);
   }
}
