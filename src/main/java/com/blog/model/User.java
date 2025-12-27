// src/main/java/com/blog/model/User.java

package com.blog.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
@Data
public class User {

    private Integer id; // 用户ID
    private String username; // 用户名

    @JsonIgnore // 在序列化（返回给前端）时忽略密码字段，保障安全
    private String password; // 密码（加密存储）

    private String nickname; // 昵称
    private String email; // 邮箱
    private String avatar; // 头像
    private String role; // 角色：admin/author
    private Integer status; // 状态：0-禁用，1-启用

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime; // 更新时间

    // 构造函数 (Constructor)
    public User() {}

    // Getter 和 Setter 方法 (省略部分代码，你需要手动补全)
    // IntelliJ IDEA 中可使用 Alt+Insert 或右键 Generate 快速生成

    // 示例：getUsername 和 setUsername
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // ... 其他 Getter/Setter ...

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 建议：重写 toString 方法方便调试
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                // 确保不要打印密码！
                ", role='" + role + '\'' +
                ", status=" + status +
                '}';
    }
}