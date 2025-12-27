package com.blog.util;

import java.security.MessageDigest;

public class EncryptUtil {
    // ⚠️ 关键点：这个盐值必须和 SQL 里生成密码时用的一模一样
    private static final String SALT = "blog_salt";

    public static String encrypt(String password) {
        try {
            // 密码 + 盐 混合
            String str = password + SALT;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes("UTF-8"));

            // 转成 16 进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}