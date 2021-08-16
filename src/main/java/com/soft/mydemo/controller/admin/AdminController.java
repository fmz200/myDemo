package com.soft.mydemo.controller.admin;

import com.soft.mydemo.bean.RespBean;
import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/article/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByStateByAdmin(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "count", defaultValue = "6") Integer pageSize,
                                                        String keywords) {
        log.info("admin.article.all start... keywords is {}, page is {}, count is {}", keywords, pageNum, pageSize);
        return articleService.getArticleByState(CommonConstants.STATE_ARTICLE_DRAFT1, pageNum, pageSize, keywords);
    }

    @RequestMapping(value = "/article/dustbin", method = RequestMethod.PUT)
    public RespBean updateArticleState(Long[] aids, Integer state) {
        if (articleService.updateArticleState(aids, state) == aids.length) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }
}
