package com.soft.mydemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soft.mydemo.util.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微博控制类
 *
 * @date 2022/01/20
 */
@Slf4j
@Controller
@RequestMapping(value = "/weibo")
public class WeiBoController {

    // 微博热搜url
    public static final String HOT_SEARCH_URL = "https://weibo.com/ajax/side/hotSearch";

    @ResponseBody
    @RequestMapping(value = "hotSearch")
    public JSONObject hotSearch() {
        String getResult = HttpRequest.sendGet(HOT_SEARCH_URL, "");
        log.info("hotSearch.getResult is {}", getResult);
        return JSON.parseObject(getResult);
    }
}
