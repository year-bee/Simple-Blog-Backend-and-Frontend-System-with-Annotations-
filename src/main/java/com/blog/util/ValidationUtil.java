// src/main/java/com/blog/util/ValidationUtil.java

package com.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验工具类
 */
public class ValidationUtil {

    // 邮箱格式正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 校验字符串是否为空 (null 或 空字符串)
     * @param str 待校验的字符串
     * @return 如果为 null 或 空字符串，返回 true；否则返回 false
     */
    public static boolean isEmpty(String str) {
        // 使用 trim() 排除只包含空格的情况
        return str == null || str.trim().length() == 0;
    }

    /**
     * 校验对象是否为空
     * @param obj 待校验的对象
     * @return 如果为 null，返回 true；否则返回 false
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 校验用户名是否合法 (例如：长度在 4 到 20 之间，只包含字母和数字)
     * @param username 待校验的用户名
     * @return 合法返回 true，否则返回 false
     */
    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) {
            return false;
        }
        // 示例：4-20个字符，只能包含字母、数字和下划线
        return username.matches("^[a-zA-Z0-9_]{4,20}$");
    }

    /**
     * 校验密码是否满足复杂度要求 (例如：长度在 6 到 30 之间)
     * @param password 待校验的密码
     * @return 合法返回 true，否则返回 false
     */
    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        // 示例：长度 6-30
        return password.length() >= 6 && password.length() <= 30;
    }

    /**
     * 校验邮箱格式是否正确
     * @param email 待校验的邮箱
     * @return 格式正确返回 true，否则返回 false
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * 校验ID是否为正整数
     * @param id 待校验的ID
     * @return 如果ID大于0，返回 true；否则返回 false
     */
    public static boolean isValidId(Integer id) {
        return id != null && id > 0;
    }

    // 可以根据需要添加更多校验方法，例如：
    // public static boolean isValidUrl(String url) {}
    // public static boolean isNumeric(String str) {}
}