// src/main/java/com/blog/util/DBUtil.java

package com.blog.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static DataSource dataSource;

    // 静态代码块：类加载时初始化 Druid 连接池
    static {
        try {
            Properties properties = new Properties();
            // 假设 db.properties 文件放在 src/main/resources 目录下
            InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(is);

            dataSource = DruidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            System.err.println("数据库连接池初始化失败！");
            e.printStackTrace();
            // 确保应用在连接池初始化失败时停止
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭资源
     */
    public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            // 连接放回连接池
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("关闭数据库资源失败！");
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源 (没有 ResultSet 的情况)
     */
    public static void close(PreparedStatement ps, Connection conn) {
        close(null, ps, conn);
    }
}