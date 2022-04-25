package com.soft.mydemo.controller;

import com.soft.mydemo.bean.SalesInfoBean;
import com.soft.mydemo.bean.SalesInfoBeanResp;
import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.service.SalesInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Controller
@RequestMapping(value = "/hello")
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Resource
    private SalesInfoService salesInfoService;

    @RequestMapping("/index")
    public String sayHello() {
        return "index";
    }

    @RequestMapping("/success")
    public String saySuccess() {
        return CommonConstants.SUCCESS;
    }

    @RequestMapping("/error")
    public String sayError() {
        return CommonConstants.ERROR;
    }

    @ResponseBody
    @RequestMapping(value = "querySalesInfo", method = RequestMethod.POST)
    public SalesInfoBeanResp querySalesInfo(SalesInfoBean salesInfoBeanReq) {
        logger.debug("querySalesInfo start... salesInfoBeanReq is {}", salesInfoBeanReq);
        SalesInfoBeanResp salesInfoBeanResp = salesInfoService.querySalesInfo(salesInfoBeanReq.getSaleId());
        logger.debug("querySalesInfo end... salesInfoBeanResp is {}", salesInfoBeanResp);
        return salesInfoBeanResp;
    }
}
