package com.example.myprojectbackend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class StringUtil {

    /**
     * 判断是否是空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否不是空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if ((str != null) && !"".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化模糊查询
     *
     * @param str
     * @return
     */
    public static String formatLike(String str) {
        if (isNotEmpty(str)) {
            return "%" + str + "%";
        } else {
            return null;
        }
    }

    /**
     * 过滤掉集合里的空格
     *
     * @param list
     * @return
     */
    public static List<String> filterWhite(List<String> list) {
        List<String> resultList = new ArrayList<String>();
        for (String l : list) {
            if (isNotEmpty(l)) {
                resultList.add(l);
            }
        }
        return resultList;
    }

    /**
     * 去除html标签
     */
    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 去掉空格
        content = content.replaceAll(" ", "");
        return content;
    }

    /**
     * 生成六位随机数
     *
     * @return
     */
    public static String genSixRandomNum() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    /**
     * 生成由[A-Z,0-9]生成的随机字符串
     *
     * @param length 欲生成的字符串长度
     * @return
     */
    public static String getRandomString(int length) {
        Random random = new Random();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(2);
            long result = 0;

            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char) result));
                    break;
                case 1:

                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }


    public static String modifyString(String input, String type) {
        // 檢查字串是否以 "index" 結尾

        switch (type) {
            case "M":
                // 如果是以"/" 為結尾代表，代表輸入錯誤，把結尾斜線去除
                if (input.endsWith("/"))
                    input = input.substring(0, input.length() - 1);

                //處理目錄類型的路徑
                // 如果是以"/" 則不用動作
                if (input.startsWith("/")) {
                    return input;
                } else {
                    // 如果不是以 "/" 為開頭，直接在前面加上 "/"
                    return "/" + input;
                }

            case "C":
                if (input.endsWith("index")) {
                    // 去除 "index" 並在前面加上 "/"
                    input = input.substring(0, input.length() - "/index".length());
                }

                return "/" + input;

        }
       return  "";
    }

    public  static String  genurateUserId(){

        UUID userId=UUID.randomUUID();
        return userId.toString().replace("-", "");

    }

}

