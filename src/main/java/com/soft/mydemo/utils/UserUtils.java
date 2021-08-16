package com.soft.mydemo.utils;

import com.soft.mydemo.bean.UserInfoBean;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by fmz200 on 2021/08/07
 */
public class UserUtils {
    public static UserInfoBean getCurrentUser() {
        return (UserInfoBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
