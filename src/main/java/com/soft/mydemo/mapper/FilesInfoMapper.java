package com.soft.mydemo.mapper;

import com.soft.mydemo.bean.filesInfo.FilesInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FilesInfoMapper {

    List<String> getFileTypes();

    List<FilesInfoBean> queryFilesInfo(FilesInfoBean interfaceInfoBean);

    void insertFilesInfo(FilesInfoBean interfaceInfoBean);

    FilesInfoBean queryFilesInfoForOne(String fileId);

    void updateFileDownloadTimes(String fileId);

    void updateFileState(@Param("fileIdList") List<String> fileIdList, @Param("state") String state);
}
