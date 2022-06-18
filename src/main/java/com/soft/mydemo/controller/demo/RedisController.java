package com.soft.mydemo.controller.demo;

import com.soft.mydemo.service.UserService;
import com.soft.mydemo.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "/hello/{id}")
    public String hello(@PathVariable(value = "id") String id) {
        log.debug("redis.hello start...");
        // 查询缓存中是否存在
        boolean hasKey = redisUtils.exists(id);
        String str;
        if (hasKey) {
            //获取缓存
            Object object = redisUtils.get(id);
            log.info("从缓存获取的数据：{}", object);
            str = object.toString();
        } else {
            //从数据库中获取信息
            log.info("从数据库中获取数据...");
            UserDetails user = userService.loadUserByUsername(id);
            // 数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id, user, 10L, TimeUnit.MINUTES);
            str = user.toString();
            log.info("数据插入缓存:{}", user);
        }
        log.debug("redis.hello end...");
        return str;
    }
}

