package com.soft.mydemo.bean;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by fmz200 on 2021/08/07.
 */
@Data
public class CategoryInfoBean {
    private Long id;
    private String cateName;
    private Timestamp date;

}
