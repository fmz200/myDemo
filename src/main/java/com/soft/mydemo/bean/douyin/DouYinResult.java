package com.soft.mydemo.bean.douyin;

import lombok.Data;

import java.util.List;

/**
 * Created by fmz200 on 2021/08/07
 */
@Data
public class DouYinResult {
    private Long id;
    private String name;
    private List<DouYinInfoBean> item_list;

}
