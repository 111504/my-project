package com.example.myprojectbackend.utils;
import org.springframework.data.redis.core.StringRedisTemplate;
/*驗證碼請求限流*/

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtils {


        @Resource
        StringRedisTemplate template;

        public boolean limitOnceCheck(String key,int blockTime){
            if(Boolean.TRUE.equals(template.hasKey(key))){
              return  false;
            }else{
                 template.opsForValue().set(key,"",blockTime, TimeUnit.SECONDS);
                 return true;
            }

        }





}
