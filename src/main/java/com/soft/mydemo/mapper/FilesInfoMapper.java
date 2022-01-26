package com.soft.mydemo.mapper;

import com.soft.mydemo.bean.filesInfo.FilesInfoBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FilesInfoMapper {

    List<FilesInfoBean> queryFilesInfo(FilesInfoBean interfaceInfoBean);

    void insertFilesInfo(FilesInfoBean interfaceInfoBean);

    FilesInfoBean queryFilesInfoForOne(String fileId);

    void updateFileDownloadTimes(String fileId);
}
