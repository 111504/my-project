package com.example.myprojectbackend.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myprojectbackend.controller.SysRoleController;
import com.example.myprojectbackend.dao.SysUserRepository;
import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.entity.AccountEntity;
import com.example.myprojectbackend.entity.UserTokenEntity;
import com.example.myprojectbackend.dao.AccountRepository;
import com.example.myprojectbackend.dao.UserTokenRepository;
import com.example.myprojectbackend.entity.system.SysRole;
import com.example.myprojectbackend.entity.system.SysUserEntity;
import com.example.myprojectbackend.service.SysRoleService;
import com.example.myprojectbackend.vo.reponse.AuthorizeVO;

import com.example.myprojectbackend.filter.JwtAuthorizeFilter;
import com.example.myprojectbackend.service.AccountService;
import com.example.myprojectbackend.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;


@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    AccountService service;

    @Resource
    UserTokenRepository userTokenRepository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    SysUserRepository sysUserRepository;

    @Resource
    SysRoleService sysRoleService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        return http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                                authorizeHttpRequests
                        //對於匹配 /api/auth/** 這個路徑模式的所有請求，都允許所有用戶（包括未經認證的用戶）訪問。
                        .requestMatchers("/api/auth/**").permitAll()
                                        .requestMatchers("/api/picture/**").permitAll()
                                        .requestMatchers("/api/android/**").permitAll()
                                //測試用api 不需要權限認證
                                .requestMatchers("/api/test/**").permitAll()
                                //對於那些沒有被前面的規則匹配到的請求，都需要用戶進行認證才能訪問
                        .anyRequest().authenticated()
                        //允許表單登入
                ).formLogin(conf->conf
                        .loginProcessingUrl("/api/auth/login")
                        //登入失敗
                        .failureHandler(this::onAuthenticationFailure)
                        //登入成功
                        .successHandler(this::onAuthenticationSuccess)

                ).logout(conf->conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf->conf
                        //當用戶未認證的情況下，嘗試訪問需要認證的資源時，要調用的處理程序。(例如用戶處於未登入)
                        .authenticationEntryPoint(this::onUnauthorized )
                        //設定了當用戶已驗證，但是沒有權限訪問資源時要調用的處理程序(授權)
                        .accessDeniedHandler(this::onAccessDeny)
                )
                .csrf(AbstractHttpConfigurer::disable)
                /*這個配置將 Session 管理模式設定為「無狀態」(STATELESS)。這意味著：
                  服務端不為每個客戶端建立 HTTP Session，也就沒有了需要維護與傳輸 Session ID 的負擔。
                  驗證信息需要透過每次請求傳遞，通常使用像是 JWT (JSON Web Token) 的形式。*/
                .sessionManagement(conf->conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 這行將自定義過濾器 jwtAuthorizeFilter 加入到 Spring Security 過濾器鏈中，
                // 並確保它在預設的使用者名/密碼驗證過濾器 (UsernamePasswordAuthenticationFilter) 之前執行。

                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }




    //回傳Status  403  forbidden
    private void onAccessDeny(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        System.out.println("Access Denied");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden(e.getMessage()).asJsonString());
    }






        public void onUnauthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException exception) throws IOException {
            System.out.println("Unauthorized");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());



    }
    public void onAuthenticationSuccess(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        System.out.println("onAuthenticationSuccess");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String username=authentication.getName();
        SysUserEntity  sysUserEntity= sysUserRepository.findAccountByNameOrEmail(username);
        UUID tokenId=UUID.randomUUID();
        String token=utils.createJwt(sysUserEntity.getUuid(), sysUserEntity.getUsername(),tokenId);

        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id=" + sysUserEntity.getId()));
        String role = "";
        for(SysRole sysRole:roleList){

            role+=sysRole.getCode().toString()+" ";
        }
        AuthorizeVO vo=new AuthorizeVO();
        vo.setToken(token);
        vo.setUuid(sysUserEntity.getUuid());
        vo.setUsername(sysUserEntity.getUsername());
        vo.setExpire(utils.expireTime());
        vo.setTokenId(tokenId.toString());
        vo.setAuthorization(authentication.getAuthorities().toString());
        vo.setEmail(sysUserEntity.getEmail());
        vo.setPhoneNumber(sysUserEntity.getPhoneNumber());
        vo.setRole(role);
        vo.setLoginDate(sysUserEntity.getLoginDate());
        vo.setId(sysUserEntity.getId());
        vo.setAvatar(sysUserEntity.getAvatar());
        //回傳使用者的紀錄
        List numberOfUser= userTokenRepository.checkUserExist(sysUserEntity.getUuid());
        if(numberOfUser.size()>1){
            //先搜尋使用者是否有紀錄，如果有出現，代表用戶不是第一次登入，那麼進行鎖定該用戶token操作
            userTokenRepository.lockToken(sysUserEntity.getUuid());
            System.out.println("success lock token");
        }
        //儲存token進資料庫
        userTokenRepository.save(new UserTokenEntity(sysUserEntity.getUuid(),tokenId,1,utils.expireTime()));
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }

    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        System.out.println("onLogoutSuccess");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer=response.getWriter();
        String authorization=request.getHeader("Authorization");
        if(utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success().asJsonString());
        }else{
            writer.write(RestBean.failure(400,"退出登錄失敗").asJsonString());
        }
    }
}
