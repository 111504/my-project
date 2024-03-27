package com.example.myprojectbackend.service.impl;


import com.example.myprojectbackend.entity.AccountEntity;
import com.example.myprojectbackend.dao.AccountRepository;
import com.example.myprojectbackend.service.SysUserService;
import com.example.myprojectbackend.vo.request.ConfirmResetVO;
import com.example.myprojectbackend.vo.request.EmailRegisterVO;
import com.example.myprojectbackend.vo.request.EmailResetVO;
import com.example.myprojectbackend.service.AccountService;
import com.example.myprojectbackend.utils.Const;
import com.example.myprojectbackend.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    FlowUtils utils;

    @Resource
    AmqpTemplate amqpTemplate;


    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    SysUserService sysUserService;


   private final AccountRepository accountRepository;

   public AccountServiceImpl(AccountRepository accountRepository){
       this.accountRepository = accountRepository;
   }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("開始驗證使用者身分");
        Optional<Integer> result=accountRepository.checkAccountEnabled(username);
        if (result.isEmpty() || result.get() == 0) {
            // 如果找不到结果，或账户被禁用，直接抛出异常
            throw new UsernameNotFoundException("用戶名錯誤或帳戶已經被禁用");
        }

        AccountEntity accountEntity = accountRepository.findAccountByNameOrEmail(username);
        if(accountEntity == null)
            throw new UsernameNotFoundException("用戶名或密碼錯誤");

        String rolesStr[]= accountEntity.getRole().split(",");
        System.out.println("Roles: " + rolesStr);


        return new User(accountEntity.getUsername(),accountEntity.getPassword(),getUserAuthority(2L));

//        return User
//                .withUsername(username)
//                .password(accountEntity.getPassword())
//                //從資料庫查詢到的使用者角色，填入User後回傳(authorities)
//                .roles(rolesStr)
//                .build();
    }

    @Override
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        //  格式ROLE_admin,ROLE_common,system:user:resetPwd,system:role:delete,system:user:list,system:menu:query,system:menu:list,system:menu:add,system:user:delete,system:role:list,system:role:menu,system:user:edit,system:user:query,system:role:edit,system:user:add,system:user:role,system:menu:delete,system:role:add,system:role:query,system:menu:edit
        String authority=sysUserService.getUserAuthorityInfo(userId);
        //AuthorityUtils.commaSeparatedStringToAuthorityList 是 Spring Security 提供的一个工具方法，用于将逗号分隔的权限字符串转换成一个 List<GrantedAuthority>
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        //請求驗證碼的冷卻時間
        // 單線程，因為ip同時間內只能有一個對驗證碼的請求，避免此ip同時間多開網頁，發送大量請求
        //synchronized (ip.intern()){
        //避免同ip 不斷發送請求，設定ip請求的冷卻時間
    //    if(!verityLimit(ip))
    //      return  "請求頻繁 請稍後在試";
        int verifyCode=generatorVerifyCode(100000,999999);
        Map<String,Object> data= Map.of("type",type,"email",email,"code",verifyCode);
        //}
        amqpTemplate.convertAndSend("mail",data);
        //把驗證碼與信箱做關聯，設定驗證有效時間



        try {
            stringRedisTemplate.opsForValue()
                .set(Const.VERIFY_EMAIL_DATA +email,String.valueOf(verifyCode),3, TimeUnit.MINUTES);}
        catch (Exception e){
            return "Redius error: " + e.getMessage();
        }
        return "SUCCESS";
    }
    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        System.out.println("開始註冊帳號流程");
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

        if(accountRepository.findByUsername(username)!=null) {
            return "此用戶名已被使用";
        }

        Optional<Integer> enabledOpt= accountRepository.checkAccountEnabled(email);

        if (enabledOpt.isPresent()) {
            Integer isEnabled = enabledOpt.get();
            if (isEnabled==1) {
                return "該信箱 " + email + " 已使用中.請更換";
            } else {
                System.out.println("該信箱停用中 " + email + " 開始啟用.");
                String password=passwordEncoder.encode(vo.getPassword());
                ZonedDateTime nowInZone = ZonedDateTime.now();
                if(accountRepository.enableAccount(username,password,"user", nowInZone.toLocalDateTime(),userUuid,true,email)>0){
                    System.out.println("帳號啟用成功");
                    return "SUCCESS";
                }
                else{
                    return "帳號啟用失敗";
                }

            }
        } else {
            System.out.println("開始創建帳號 ");
            String password=passwordEncoder.encode(vo.getPassword());
            ZonedDateTime nowInZone = ZonedDateTime.now();
            AccountEntity accountEntity =new AccountEntity(username,password,email,"user",nowInZone.toLocalDateTime(),userUuid,true);


            try {
                accountRepository.save(accountEntity);
                return "SUCCESS";

            } catch (DataIntegrityViolationException e) {
                // 处理违反数据完整性的情况，比如重复的键等
              //  e.printStackTrace();
                // 返回null或者自定义错误对象
                return "DataIntegrityViolation Exception";
            } catch (Exception e) {
                // 捕获其他所有异常
               // e.printStackTrace();
                // 返回null或者自定义错误对象
                return "RegisterEmailAccount Exception";
            }
        }

    }
    private int generatorVerifyCode(int min,int max){
        Random random=new Random();

        return random.nextInt(max) + min;
    }



    @Override
    public String resetConfirm(ConfirmResetVO vo) {
        String email=vo.getEmail();
        String code=stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA+email);
        if(code==null) return "請先獲取驗整碼";
        if(!code.equals(vo.getCode())) return "驗整碼錯誤，請重新輸入";

        return "SUCCESS";
    }

    @Override
    public String resetEmailAccountPassword(EmailResetVO vo) {
        String email=vo.getEmail();
        String verify=this.resetConfirm(new ConfirmResetVO(email, vo.getCode()));
        if(!Objects.equals(verify, "SUCCESS")) return verify;
        String password=passwordEncoder.encode(vo.getPassword());
        if(accountRepository.updatePasswordByEmail( password,email)>0){
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA+email);
            return "SUCCESS";
        }else{
            return "重置密碼 失敗";
        }


    }


    //避免同ip 不斷發送請求
    private boolean verityLimit(String ip){
        String key= Const.VERIFY_EMAIL_LIMIT+ip;
        return utils.limitOnceCheck(key,10);
    }

    public enum AccountStatus {
        ENABLED, DISABLED, NOT_FOUND
    }

}
