// src/main/java/com/blog/common/Result.java

package com.blog.common;

/**
 * 统一API响应格式
 * @param <T> 数据类型
 */
public class Result<T> {
    private int code;    // 状态码：200成功，其他失败
    private String msg;  // 消息
    private T data;      // 响应数据

    // 私有构造函数，强制通过静态方法创建实例
    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功方法
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // 失败方法
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    // Getter 和 Setter (省略，请使用 IDE 自动生成)
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public T getData() {
        return data;
    }
}