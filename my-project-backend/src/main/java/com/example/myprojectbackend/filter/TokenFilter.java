package com.example.myprojectbackend.filter;

import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.dao.UserTokenRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(-101)
public class TokenFilter extends HttpFilter {


    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    UserTokenRepository userTokenRepository;
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)throws IOException, ServletException {

        System.out.println("進入在CROS之後 Security之前");
        //有些api請求式不需要進行tokem認證
        String jwtTokenId = request.getHeader("JwtTokenId");
        if (isValidUUID(jwtTokenId) ) {
            if(!userTokenRepository.checkTokenFlag(jwtTokenId)) {
                System.out.println("token失效");

                response.getWriter().write(RestBean.success("TOKEN_DISABLE", "Access denied,Token is disable").asJsonString());
                return;
            }
        }



        jwtAuthorizeFilter.printRequestAttributes(request);

        chain.doFilter(request,response);
    }

    public static boolean isValidUUID(String uuid) {
        if (uuid == null) {
            return false;
        }
        return uuid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }
}
