package com.soft.mydemo.controller;

import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.service.SalesInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
