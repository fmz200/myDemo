package com.soft.mydemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.soft.mydemo.bean.RoleInfoBean;
import com.soft.mydemo.bean.UserInfoBean;

import java.util.List;

/**
 * Created by fmz200 on 2021/08/07
 *
 * integrity
 */
@Mapper
public interface UserMapper {

    UserInfoBean loadUserByUsername(@Param("username") String username);

    long reg(UserInfoBean user);

    int updateUserEmail(@Param("email") String email, @Param("id") Long id);

    List<UserInfoBean> getUserByNickname(@Param("nickname") String nickname);

    List<RoleInfoBean> getAllRole();

    int updateUserEnabled(@Param("enabled") Boolean enabled, @Param("uid") Long uid);

    int deleteUserById(Long uid);

    int deleteUserRolesByUid(Long id);

    int setUserRoles(@Param("rids") Long[] rids, @Param("id") Long id);

    UserInfoBean getUserById(@Param("id") Long id);
}
