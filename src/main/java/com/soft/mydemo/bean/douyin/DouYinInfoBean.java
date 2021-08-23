package com.soft.mydemo.bean.douyin;

import lombok.Data;

import java.util.List;

/**
 * Created by fmz200 on 2021/08/07
 */
@Data
public class DouYinInfoBean {
    private DouYinUrlBean video;
    private DouYinUrlBean music;
    private String desc;
    private String uri;

    private List<String> url_list;
}
