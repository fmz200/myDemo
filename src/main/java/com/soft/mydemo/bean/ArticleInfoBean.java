package com.soft.mydemo.bean;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by fmz200 on 2021/08/07.
 */
@Data
public class ArticleInfoBean {
    private Long id;
    private String title;
    private String mdContent;
    private String htmlContent;
    private String summary;
    private Long cid;
    private Long uid;
    private Timestamp publishDate;
    private Integer state;
    private Integer pageView;
    private Timestamp editTime;
    private String[] dynamicTags;
    private String nickname;
    private String cateName;
    private List<TagsInfoBean> tags;
    private String stateStr;
    private String source; // 页面操作类型，从哪个页面跳转而来
    private String publishDateStart;
    private String publishDateEnd;
    private String editTimeStart;
    private String editTimeEnd;

    private Integer page; // pageNum
    private Integer count; // pageSize
}
