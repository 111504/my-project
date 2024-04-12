package com.example.myprojectbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.myprojectbackend.dao.UserTokenRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    @Value("${spring.security.jwt.key}")
    String key;


    @Value("${spring.security.jwt.expire}")
    int expire;

    @Resource
    StringRedisTemplate template;


    @Resource
    UserTokenRepository userTokenRepository;

    public boolean invalidateJwt(String headerToken){
        String token=this.convertToken(headerToken);
        System.out.println("invalidateJwt token="+token);
        if(token==null) return false;
        Algorithm algorithm=Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier=JWT.require(algorithm).build();
        try{
            //解密
            DecodedJWT jwt=jwtVerifier.verify(token);
            Map<String , Claim> claims=jwt.getClaims();
            String tokenId = claims.get("jti").asString();
            System.out.println("tokenId="+tokenId);
            if(userTokenRepository.lockTokenByTokenId(tokenId)>0){
                System.out.println("success lock token");
            }else{
                System.out.println("fail lock token");
                return  false;
            }

            String id=jwt.getId();
            System.out.println("DecodedJWT jwt id="+jwt.getId());
            return  deleteToken(id,jwt.getExpiresAt());
        }
        catch (JWTVerificationException e){
              return  false;
        }
    }





    private boolean deleteToken(String uuid,Date time){
        System.out.println("deleteToken uuid= "+uuid+", result= "+this.isInvalidToken(uuid));
        if(this.isInvalidToken(uuid))
            return false;
        Date now=new Date();
        long expire=Math.max(time.getTime()- now.getTime(),0);
        template.opsForValue().set(Const.JWT_BLACK_LIST+uuid,"",expire, TimeUnit.MILLISECONDS);
        return true;
    }

    private boolean isInvalidToken(String uuid){
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST+uuid));
    }
    public DecodedJWT resolveJwt(String headerToken){
        String token=this.convertToken(headerToken);

        if(token==null) return null;
        Algorithm algorithm=Algorithm.HMAC256(key);

        JWTVerifier jwtVerifier=JWT.require(algorithm).build();

        try{
            DecodedJWT verify=jwtVerifier.verify(token);

            if(this.isInvalidToken(verify.getId()))
                return null;
            Date expiresAt=verify.getExpiresAt();
                return  new Date().after(expiresAt)?null:verify;
        }catch(JWTVerificationException e){
            return null;
        }
    }

    /*
    * token生成，傳入使用者資料，id，使用者名稱
    *
    * */
    public String createJwt(String uuid, String username,UUID tokenId){


       //賦予每一個jwt 不重複的id值



        //選擇演算法HMAC256
        Algorithm algorithm=Algorithm.HMAC256(key);
        //Date expire=this.expireTime();
        //設定時區
        LocalDateTime localDateTime = LocalDateTime.now();


        Date dateTimeNow=Date
                .from(localDateTime.atZone(ZoneId.systemDefault())
                .toInstant());

        System.out.println("createJwt  使用者uuid= "+uuid);
        System.out.println("createJwt  使用者名稱= "+username);
        System.out.println("createJwt  當前時間="+dateTimeNow);
        System.out.println("createJwt 不重複的JwtUuid="+tokenId);
        System.out.println("createJwt  過期時間="+expireTime());
 //       System.out.println("createJwt  用戶身份="+role);
//        System.out.println("createJwt  用戶授權="+details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        return JWT.create()
                //賦予使用者的uuid
                .withClaim("uuid",uuid)
                //賦予每一個jwt 不重複的id值
                .withJWTId(String.valueOf(tokenId))
                .withClaim("name",username)
//                //屬性 authorities
//                .withClaim("authorities",details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                //jwt過期時間
                .withExpiresAt(expireTime())
                 //簽發時間
                .withIssuedAt(dateTimeNow)
                 //使用者身份
           //     .withClaim("role", role)
                //獲取jwt的加密演算法
                .sign(algorithm);
    }
    //指定token的過期時間，首先獲取當前時間，再指定要過多久後該token過期
    public Date expireTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime,ZoneId.systemDefault());


        System.out.println("token過期時間="+Date.from(zdt.plusMinutes(30).toInstant()));
        return Date.from(zdt.plusMinutes(30).toInstant());
    }

    public UserDetails toUser(DecodedJWT jwt){
        Map<String , Claim> claims=jwt.getClaims();

        String rolesStr[] = claims.get("role").asString().split(",");

        return User
                .withUsername(claims.get("name").asString())
                .password("******")
                .roles(rolesStr)
                .build();
    }

    public  String toId(DecodedJWT jwt){
        Map<String,Claim> claims=jwt.getClaims();
        return claims.get("uuid").toString();
    }



    private String convertToken(String headerToken){
        if(headerToken==null||!headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }
}
