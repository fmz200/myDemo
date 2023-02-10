package com.soft.mydemo.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.soft.mydemo.bean.user.SalesInfoBean;
import com.soft.mydemo.bean.user.SalesInfoBeanReq;
import com.soft.mydemo.bean.user.SalesInfoBeanResp;
import com.soft.mydemo.mapper.SaleInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SalesInfoService {
    @Autowired
    private SaleInfoMapper saleInfoMapper;

    @RequestMapping(value = "/querySalesInfoList")
    @ResponseBody
    public Map<String, Object> querySalesInfoList(SalesInfoBeanReq queryParams) {
        log.debug("querySalesInfoList begin... queryParams is {}", JSON.toJSONString(queryParams));
        PageMethod.startPage(queryParams.getPageNum(), queryParams.getPageSize());
        List<SalesInfoBean> salesInfoList = saleInfoMapper.querySalesInfoList(queryParams);
        PageInfo<SalesInfoBean> pageInfo = new PageInfo<>(salesInfoList);

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", pageInfo.getTotal());
        map.put("salesInfoList", salesInfoList);
        log.debug("querySalesInfoList end... result is {}", JSON.toJSONString(map));
        return map;
    }

    public SalesInfoBeanResp querySalesInfoById(String saleId) {
        SalesInfoBeanResp salesInfoBeanResp = new SalesInfoBeanResp();
        try {
            com.soft.mydemo.bean.user.SalesInfoBean salesInfoBean = saleInfoMapper.querySalesInfoById(saleId);
            salesInfoBeanResp.setSalesInfoBean(salesInfoBean);
        } catch (Exception e) {
            e.printStackTrace();
            salesInfoBeanResp.setCode("-1");
            salesInfoBeanResp.setMessage("查询失败！" + e);
        }
        return salesInfoBeanResp;
    }

}
