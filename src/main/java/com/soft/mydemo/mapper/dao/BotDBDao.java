package com.soft.mydemo.mapper.dao;

import org.apache.ibatis.annotations.Mapper;

// @Repository
@Mapper
public interface BotDBDao {

    String queryDBVersion();

}
