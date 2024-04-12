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

        try {
            // 尝试获取和检查JwtTokenId
            String jwtTokenId = request.getHeader("JwtTokenId");
            if (jwtTokenId == null || jwtTokenId.isEmpty()) {
                throw new IllegalArgumentException("Request does not contain a header named 'JwtTokenId'.");
            } else {
           //     System.out.println("JwtTokenId found: " + jwtTokenId);
                // 进行JwtTokenId的处理
                if(jwtTokenId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
                    if (!userTokenRepository.checkTokenFlag(jwtTokenId)) {
                        System.out.println("token失效");

                        response.getWriter().write(RestBean.success("TOKEN_DISABLE", "Access denied,Token is disable").asJsonString());
                        return;
                    }
                }


            }
        } catch (IllegalArgumentException e) {
            // 捕获到异常，处理异常（例如打印日志）
        //    System.out.println("Exception caught: " + e.getMessage());
            // 此处可以添加你需要执行的代码，即使没有JwtTokenId头也会执行
        }

        // 即使上面的代码抛出了IllegalArgumentException，以下的代码仍然会执行
     //   System.out.println("Continuing with the rest of the code...");
        chain.doFilter(request,response);

    }


}
