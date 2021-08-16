package com.soft.mydemo.controller;

import com.soft.mydemo.bean.RespBean;
import com.soft.mydemo.service.UserService;
import com.soft.mydemo.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by fmz200 on 2021/08/07
 */
@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/currentUserName")
    public String currentUserName() {
        log.debug("UserController.currentUserName start...");
        return UserUtils.getCurrentUser().getNickname();
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId() {
        log.debug("UserController.currentUserId start...");
        return UserUtils.getCurrentUser().getId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail() {
        log.debug("UserController.currentUserEmail start...");
        return UserUtils.getCurrentUser().getEmail();
    }

    @RequestMapping("/isAdmin")
    public Boolean isAdmin() {
        log.debug("UserController.isAdmin start...");
        List<GrantedAuthority> authorities = UserUtils.getCurrentUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().contains("超级管理员")) {
                log.debug("UserController.isAdmin end with return true...");
                return true;
            }
        }
        log.debug("UserController.isAdmin end with return false...");
        return false;
    }

    @RequestMapping(value = "/updateUserEmail",method = RequestMethod.PUT)
    public RespBean updateUserEmail(String email) {
        log.debug("UserController.updateUserEmail start...");
        if (userService.updateUserEmail(email) == 1) {
            log.debug("UserController.updateUserEmail end with return success......");
            return new RespBean("success", "开启成功!");
        }
        log.debug("UserController.updateUserEmail end with return error......");
        return new RespBean("error", "开启失败!");
    }
}
