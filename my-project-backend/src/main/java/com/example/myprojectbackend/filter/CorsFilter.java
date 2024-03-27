package com.example.myprojectbackend.filter;

import com.example.myprojectbackend.utils.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
@Order(Const.ORDER_CORS)
public class CorsFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)throws IOException, ServletException {
        System.out.println("進入在CROS之後");
        this.addCorsHeader(request,response);

        chain.doFilter(request,response);
    }

    private  void addCorsHeader(HttpServletRequest request,HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods","GET,HEAD,POST,PUT,DELETE,OPTIONS,PATCH");
        response.addHeader("Access-Control-Allow-Headers","Authorization,Content-Type,JwtTokenId");
        Cookie uiColorCookie = new Cookie("color", "red");
        response.addCookie(uiColorCookie);
    }
}
