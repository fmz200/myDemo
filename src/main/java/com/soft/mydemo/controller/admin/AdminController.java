package com.soft.mydemo.controller.admin;

import com.alibaba.fastjson.JSON;
import com.soft.mydemo.bean.ArticleInfoBean;
import com.soft.mydemo.bean.RespBean;
import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 超级管理员专属Controller
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "/article/all")
    public Map<String, Object> getArticleByStateByAdmin(ArticleInfoBean params) {
        log.info("admin.article.all start... params is {}", JSON.toJSONString(params));
        return articleService.getArticleByState(params);
    }

    @PutMapping(value = "/article/dustbin")
    public RespBean updateArticleState(Long[] aids, Integer state) {
        if (articleService.updateArticleState(aids, state) == aids.length) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }
}
