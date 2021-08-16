package com.soft.mydemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.soft.mydemo.bean.RoleInfoBean;

import java.util.List;

/**
 * Created by fmz200 on 2021/08/07
 */
@Mapper
public interface RolesMapper {
    int addRoles(@Param("roles") String[] roles, @Param("uid") Long uid);

    List<RoleInfoBean> getRolesByUid(Long uid);
}
