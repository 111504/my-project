package com.example.myprojectbackend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myprojectbackend.entity.dao.Account;
import com.example.myprojectbackend.entity.vo.request.EmailRegisterVO;
import com.example.myprojectbackend.mapper.AccountMapper;
import com.example.myprojectbackend.service.AccountService;
import com.example.myprojectbackend.entity.vo.request.ConfirmResetVO;
import com.example.myprojectbackend.entity.vo.request.EmailResetVO;
import com.example.myprojectbackend.utils.Const;
import com.example.myprojectbackend.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    FlowUtils utils;

    @Resource
    AmqpTemplate amqpTemplate;


    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user");
        Account account=this.findAccountByNameOrEmail(username);
        if(account== null)
            throw new UsernameNotFoundException("用戶名或密碼錯誤");
      //  List<GrantedAuthority> authorities = new ArrayList<>(4);

        String rolesStr[] = account.getRole().split(",");
        System.out.println("Roles: " + rolesStr);         //       "admin,user,editor";




        return User
                .withUsername(username)
                .password(account.getPassword())

                //從資料庫查詢到的使用者角色，填入User後回傳(authorities)
                .roles(rolesStr)
                .build();
    }
    public Account findAccountByNameOrEmail(String text){
        return  this.query()
                .eq("username",text).or()
                .eq("email",text).one();
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        //請求驗證碼的冷卻時間
        //單線程，因為ip同時間內只能有一個對驗證碼的請求，避免此ip同時間多開網頁，發送大量請求
    //    synchronized (ip.intern()){
            System.out.println("ip="+ip);
            //避免同ip 不斷發送請求，設定ip請求的冷卻時間
//            if(!verityLimit(ip))
//                return  "請求頻繁 請稍後在試";




            int verifyCode=generatorVerifyCode(100000,999999);

            Map<String,Object>data= Map.of("type",type,"email",email,"code",verifyCode);
            amqpTemplate.convertAndSend("mail",data);
            //把驗證碼與信箱做關聯，設定驗證碼冷卻時間
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA +email,String.valueOf(verifyCode),3, TimeUnit.MINUTES);


            return null;
   //    }

    }

    //註冊

    public String registerEmailAccount(EmailRegisterVO vo) {
        System.out.println("開始註冊帳號");
        String email = vo.getEmail();
        System.out.println("email="+email);
        String username = vo.getUsername();
        String key=Const.VERIFY_EMAIL_DATA+email;
        String code=stringRedisTemplate.opsForValue().get(key);
        System.out.println("註冊碼="+code);
        //產生員工uuid值
        UUID uuid = UUID.randomUUID();
        String userUuid=uuid.toString();

        if(code==null) return "請先取得驗證碼";
        if(!code.equals(vo.getCode())) return "驗證碼輸入錯誤，請重新輸入";
        if(this.existAccoutByEmail(email)) return "此信箱已被使用";
        if(this.existAccoutByUsername(username)) return "此用戶名已被使用";
        //密碼加密
        String password=passwordEncoder.encode(vo.getPassword());
        Account account=new Account(null,username,password,email,"user",new Date(),userUuid);
        if(this.save(account)){
            stringRedisTemplate.delete(key);
            return null;
        }
        else{
            return "創建錯誤，請聯絡管理員";
        }

    }


    //驗證驗證碼
    @Override
    public String resetConfirm(ConfirmResetVO vo) {
        String email=vo.getEmail();
        String code=stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA+email);
        if(code==null) return "請先獲取驗整碼";
        if(!code.equals(vo.getCode())) return "驗整碼錯誤，請重新輸入";

        return null;
    }
    //密碼重置實作
    @Override
    public String resetEmailAccountPassword(EmailResetVO vo) {
        String email=vo.getEmail();
        String verify=this.resetConfirm(new ConfirmResetVO(email, vo.getCode()));
        if(verify!=null) return verify;
        String password=passwordEncoder.encode(vo.getPassword());
        boolean update=this.update().eq("email",email).set("password",password).update();
        if(update){
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA+email);
        }
        return null;
    }


    private int generatorVerifyCode(int min,int max){
        Random random=new Random();

        int code = random.nextInt(max) + min;
        return code;
    }


    //避免同ip 不斷發送請求
    private boolean verityLimit(String ip){
        String key=Const.VERIFY_EMAIL_LIMIT+ip;
        return utils.limitOnceCheck(key,10);
    }
   //email是否重複出現
    private boolean existAccoutByEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email",email));
    }

    private  boolean existAccoutByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username",username));
    }




}
