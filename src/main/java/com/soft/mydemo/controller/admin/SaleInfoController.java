package com.soft.mydemo.controller.admin;

import com.soft.mydemo.bean.user.SalesInfoBeanReq;
import com.soft.mydemo.mapper.SaleInfoMapper;
import com.soft.mydemo.service.SalesInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
public class SaleInfoController {
    @Autowired
    SaleInfoMapper saleInfoMapper;

    @Autowired
    SalesInfoService salesInfoService;

    @RequestMapping(value = "/querySalesInfoList")
    @ResponseBody
    public Map<String, Object> querySalesInfoList(SalesInfoBeanReq salesInfoParams) {
        return salesInfoService.querySalesInfoList(salesInfoParams);
    }

}
