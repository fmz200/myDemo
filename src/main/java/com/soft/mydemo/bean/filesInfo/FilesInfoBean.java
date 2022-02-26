package com.soft.mydemo.bean.filesInfo;

import lombok.Data;

import java.util.List;

@Data
public class FilesInfoBean {
    // 文件唯一标识
    private String fileId;
    // 文件名
    private String fileName;
    // 文件类型
    private String fileType;
    // 文件大小
    private String fileSize;
    // 文件路径
    private String filePtah;
    // 上传时间
    private String uploadTime;
    // 修改时间
    private String editTime;
    //
    private String attrUser;
    //
    private String categoryId;
    //
    private String categoryName;

    private String downloadTimes;

    private String state;

    private String remark;

    private String content;

    // 查询条件
    private Integer pageNum;
    private Integer pageSize;
    private String uploadTimeStart;
    private String uploadTimeEnd;
    private String editTimeStart;
    private String editTimeEnd;
    private List<String> fileTypes;

}
