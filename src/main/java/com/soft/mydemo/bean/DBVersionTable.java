package com.soft.mydemo.bean;

import lombok.Data;

import java.util.List;

@Data
public class DBVersionTable {
    /**
     * 当前数据库版本
     */
    Version ver;

    /**
     * 更新至此版本需要执行sql
     */
    List<String> sqlList;
}
