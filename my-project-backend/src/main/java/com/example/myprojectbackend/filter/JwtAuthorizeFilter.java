package com.example.myprojectbackend.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.myprojectbackend.utils.Const;
import com.example.myprojectbackend.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {


    @Resource
    JwtUtils utils;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthorizeFilter");
        String authorization=null;

        try{
            authorization=request.getHeader("Authorization");

            System.out.println("authorization="+authorization);
        }catch (NullPointerException  e){
            System.out.println("authorization= null");
        }

        if(authorization!=null){
            DecodedJWT jwt=utils.resolveJwt(authorization);
            if(jwt!=null){
                //根據jwt儲存的訊息 創立User物件後填入用戶訊息
                UserDetails user=utils.toUser(jwt);

                 System.out.println("user="+user);

                 System.out.println("user Authorities="+user.getAuthorities());
                 UsernamePasswordAuthenticationToken authentication=
                         new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
//这一步骤是用来存储与认证请求相关的额外细节信息的。这通常包括请求的IP地址和Session ID等信息，这些信息可以在认证过程中或之后的安全审计中使用。
                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//SecurityContextHolder.getContext().setAuthentication(authentication);这行代码在Spring Security中扮演着至关重要的角色。
// 它将当前用户的认证信息（Authentication对象）存储到SecurityContext中，使得这个认证信息可以在整个应用的上下文中被访问和使用。
                 SecurityContextHolder.getContext().setAuthentication(authentication);
                 request.setAttribute(Const.ATTR_USER_ID,utils.toId(jwt));
            }
            else{

                System.out.println("jwt=null");
            }

        }
        printRequestAttributes(request);
        filterChain.doFilter(request,response);
    }

    public void printRequestAttributes(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getAttribute(attributeName);
            System.out.println(attributeName + ": " + attributeValue);
        }
    }
}
