package com.example.myprojectbackend.config;

import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.entity.AccountEntity;
import com.example.myprojectbackend.entity.UserTokenEntity;
import com.example.myprojectbackend.dao.AccountRepository;
import com.example.myprojectbackend.dao.UserTokenRepository;
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


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        return http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                                authorizeHttpRequests
                        //對於匹配 /api/auth/** 這個路徑模式的所有請求，都允許所有用戶（包括未經認證的用戶）訪問。
                        .requestMatchers("/api/auth/**").permitAll()
                                //該api限定只能有身份為admin才能訪問
                                .requestMatchers("/api/test/admin").hasRole("admin")
                                .requestMatchers("/api/test/hello").hasRole("user")
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

        System.out.println("----印出authentication-----------------------------------");
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("- " + authority.getAuthority());
        }

        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getDetails());
        System.out.println(authentication.getCredentials());
        System.out.println("----結束authentication-----------------------------------");
        User user=(User)authentication.getPrincipal();
        System.out.println("----印出User-----------------------------------");
        System.out.println(user.getPassword());
        System.out.println(user.getAuthorities());
        System.out.println(user.getUsername());
        //account 儲存的是，透過使用者名稱向資料庫查詢後，回傳的資料庫內容

        AccountEntity accountEntity =accountRepository.findByUsernameOrEmail(user.getUsername());


        UUID tokenId=UUID.randomUUID();
        String token=utils.createJwt(user, accountEntity.getUuid(), accountEntity.getUsername(), accountEntity.getRole(),tokenId);

        AuthorizeVO vo=new AuthorizeVO();
        vo.setToken(token);
        vo.setUuid(accountEntity.getUuid());
        vo.setRole(accountEntity.getRole());
        vo.setUsername(accountEntity.getUsername());
        vo.setExpire(utils.expireTime());
        vo.setTokenId(tokenId.toString());

        //先停止user已使用的token
        if(userTokenRepository.lockToken(accountEntity.getUuid())>0){
            System.out.println("success lock token");
        }else{
            System.out.println("fail lock token");
        }

        //儲存token進資料庫
        userTokenRepository.save(new UserTokenEntity(accountEntity.getUuid(),tokenId,1,utils.expireTime()));


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
