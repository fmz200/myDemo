package com.soft.mydemo.bean.filesInfo;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Data
public class InterfaceInfoListBean {

    // 页面调用用这个
    private String paramListString;

    // url直接调用
    private List<FilesInfoBean> filesInfoList;

    public List<String> uuidSplit() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(paramListString)) {
            return list;
        }
        StringTokenizer st = new StringTokenizer(paramListString.replace(" ", ""), ",");
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }
}
