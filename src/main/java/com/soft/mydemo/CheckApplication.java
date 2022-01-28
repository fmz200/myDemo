package com.soft.mydemo;

import com.soft.mydemo.db.DBInitHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动类，用来检查配置等
 *
 * @author magic chen
 * @date 2020/8/20 21:08
 **/
@Slf4j
@Component
public class CheckApplication implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        // 数据库check初始化
        DBInitHelper.getInstance().initDBIfNotExist();
        // 数据库版本检查更新
        log.info("数据库检查完毕！");
    }
}
