package com.example.myprojectbackend.controller;


import com.example.myprojectbackend.entity.RestBean;
import com.example.myprojectbackend.entity.system.SysMenu;
import com.example.myprojectbackend.vo.reponse.CarbonDataVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/test")
public class TestController {
    @PreAuthorize("hasRole('user')")
    @GetMapping("/hello")
    public String test() {
        return "Hwlo World!";
    }


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin")
    public String admin() {
        return "Hwlo admin!";
    }


    @GetMapping("/form")
    public String fromInformation() {
        List<CarbonDataVO> carbonDataVOList;

        carbonDataVOList = Arrays.asList(
                new CarbonDataVO(1, "January", 0.000, 256124.000, 0.000, 256124.000, "這是說明", 128574.248, "2024102251.pdf"),
                new CarbonDataVO(2, "February", 0.000, 220391.000, 0.000, 220391.000, "這是說明", 110636.248, "2024102252.pdf"),
                new CarbonDataVO(3, "March", 0.000, 278819.000, 0.000, 278819.000, "這是說明", 139967.138, "2024102253.pdf"),
                new CarbonDataVO(4, "April", 0.000, 271306.000, 0.000, 271306.000, "這是說明", 136195.612, "2024102254.pdf"),
                new CarbonDataVO(5, "May", 0.000, 326821.000, 0.000, 326821.000, "這是說明", 164064.142, "2024102255.pdf"),
                new CarbonDataVO(6, "June", 0.000, 313664.000, 0.000, 313664.000, "這是說明", 157459.248, "2024102256.pdf"),
                new CarbonDataVO(7, "July", 0.000, 327087.000, 0.000, 327087.000, "這是說明", 164964.142, "2024102257.pdf"),
                new CarbonDataVO(8, "August", 0.000, 348489.000, 0.000, 348489.000, "這是說明", 174941.338, "2024102258.pdf")
        );
        RestBean<List<CarbonDataVO>> response = RestBean.success(carbonDataVOList, "请求用户信息成功");
          return response.asJsonString();
    }


    @GetMapping("/pieChat")
    public String requestPieChat() {
        int[] numbers = new int[6]; // 定義一個長度為6的整數陣列
        Random random = new Random(); // 創建一個隨機數生成器

        // 生成隨機數並存入陣列
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(15)+1; // 產生0到99之間的隨機整數
        }
        RestBean<int[]> response = RestBean.success(numbers, "请求用户信息成功");
        return response.asJsonString();
    }

    @GetMapping("/lineChat")
    public String requestLineChat() {
        int[] numbers = new int[22]; // 定義一個長度為6的整數陣列
        Random random = new Random(); // 創建一個隨機數生成器

        // 生成隨機數並存入陣列
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(60)+1; // 產生0到99之間的隨機整數
        }
        RestBean<int[]> response = RestBean.success(numbers, "请求用户信息成功");
        return response.asJsonString();
    }

    @GetMapping("/barChat")
    public String requestBarChat() {
        int[] numbers = new int[22]; // 定義一個長度為6的整數陣列
        Random random = new Random(); // 創建一個隨機數生成器

        // 生成隨機數並存入陣列
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(30)+1; // 產生0到99之間的隨機整數
        }
        RestBean<int[]> response = RestBean.success(numbers, "请求用户信息成功");
        return response.asJsonString();
    }


}

