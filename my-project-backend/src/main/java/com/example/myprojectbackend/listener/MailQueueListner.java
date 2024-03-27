package com.example.myprojectbackend.listener;


import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListner {

    @Resource
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String,Object> data){
        System.out.println("進入sendMailMessage");
        String email= (String) data.get("email");
        Integer code= (Integer) data.get("code");


        if(email==null|| email.isEmpty()){
            return;
        }
        SimpleMailMessage message=switch (data.get("type").toString()){

            case "register"->createMessage("註冊網站","驗證碼為:"+code+"您的信箱為",email);
            case "reset"->createMessage("重置密碼","驗證碼為"+code+",有效時間三分鐘",email);
            default -> null;

        };

        System.out.println("message="+message);
        if(message==null) return;
        javaMailSender.send(message);


    }

    private SimpleMailMessage createMessage(String title,String content,String email) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(email);
        message.setSubject(title);
        message.setText(content);
        return message;
    }

}
