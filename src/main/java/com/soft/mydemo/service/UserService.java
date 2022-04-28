package com.soft.mydemo.service;

import com.soft.mydemo.bean.RoleInfoBean;
import com.soft.mydemo.bean.UserInfoBean;
import com.soft.mydemo.mapper.RolesMapper;
import com.soft.mydemo.mapper.UserMapper;
import com.soft.mydemo.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by fmz200 on 2021/08/07
 */
@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RolesMapper rolesMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.debug("loadUserByUsername start... userName is {}", userName);
        UserInfoBean user = userMapper.loadUserByUsername(userName);
        if (Objects.isNull(user)) {
            // 避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
            return new UserInfoBean();
        }
        //查询用户的角色信息，并返回存入user中
        List<RoleInfoBean> roles = rolesMapper.getRolesByUid(user.getId());
        user.setRoles(roles);
        log.debug("loadUserByUsername end...user.id is {}", user.getId());
        return user;
    }

    /**
     * 注册
     *
     * @param user 用户
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    public int reg(UserInfoBean user) {
        UserInfoBean loadUserByUsername = userMapper.loadUserByUsername(user.getUsername());
        if (loadUserByUsername != null) {
            return 1;
        }
        //插入用户,插入之前先对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);//用户可用
        long result = userMapper.reg(user);
        //配置用户的角色，默认都是普通用户
        String[] roles = new String[]{"2"};
        int i = rolesMapper.addRoles(roles, user.getId());
        boolean b = i == roles.length && result == 1;
        if (b) {
            return 0;
        } else {
            return 2;
        }
    }

    public int updateUserEmail(String email) {
        return userMapper.updateUserEmail(email, UserUtils.getCurrentUser().getId());
    }

    public List<UserInfoBean> getUserByNickname(String nickname) {
        List<UserInfoBean> userInfoBeanList = userMapper.getUserByNickname(nickname);
        for (UserInfoBean bean : userInfoBeanList) {
            bean.setPassword("");
        }
        return userInfoBeanList;
    }

    public List<RoleInfoBean> getAllRole() {
        return userMapper.getAllRole();
    }

    public int updateUserEnabled(Boolean enabled, Long uid) {
        return userMapper.updateUserEnabled(enabled, uid);
    }

    public int deleteUserById(Long uid) {
        return userMapper.deleteUserById(uid);
    }

    public int updateUserRoles(Long[] rids, Long id) {
        userMapper.deleteUserRolesByUid(id);
        return userMapper.setUserRoles(rids, id);
    }

    public UserInfoBean getUserById(Long id) {
        UserInfoBean user = userMapper.getUserById(id);
        user.setPassword("");
        return user;
    }
}
