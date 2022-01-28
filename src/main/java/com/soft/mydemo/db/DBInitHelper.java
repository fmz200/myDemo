package com.soft.mydemo.db;

import com.soft.mydemo.util.PathUtils;
import com.soft.mydemo.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 数据库初始化工具
 *
 * @author magic chen
 * @date 2020/8/20 15:08
 **/
@Slf4j
public class DBInitHelper {

    private static final String BLOG_DATABASE = "BlogDatabase.db";
    // 内部静态类构造单例
    private DBInitHelper() {
    }

    private static class Lazy {
        private static final DBInitHelper instance = new DBInitHelper();
    }

    public static final DBInitHelper getInstance() {
        return Lazy.instance;
    }

    // 如果还没有初始化数据库，则初始化数据库
    public void initDBIfNotExist() {
        File db = new File(PathUtils.getPath() + BLOG_DATABASE);
        if (db.exists()) {
            log.info("已经找到数据文件!");
        } else {
            log.info("未找到数据文件,即将初始化数据文件!");
            createDB();
            log.info("已创建数据文件:" + PathUtils.getPath() + BLOG_DATABASE);
        }
    }


    // 创建数据库文件
    private void createDB() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            log.error("createDB notFound...", e1);
        }
        String url = "jdbc:sqlite:" + PathUtils.getPath() + BLOG_DATABASE;

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DROP TABLE IF EXISTS DBInfo;");

            String createDBSql = "create table DBInfo("
                    + "id INTEGER PRIMARY KEY autoincrement,"
                    + "name varchar(255) ,"
                    + "version varchar(20),"
                    + "createDate varchar(20),"
                    + "updateDate varchar(20));";
            statement.executeUpdate(createDBSql);

            String initSql = "INSERT INTO DBInfo (name, version, createDate, updateDate) VALUES ("
                    + "'MagicBot',"
                    + "'0.0.1',"
                    + "'" + TimeUtils.getCurrDateString() + "',"
                    + "'')";
            statement.executeUpdate(initSql);
        } catch (Exception e) {
            log.error("createDB error...", e);
        }
    }

}
