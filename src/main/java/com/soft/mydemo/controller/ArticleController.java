package com.soft.mydemo.controller;

import com.alibaba.fastjson.JSON;
import com.soft.mydemo.bean.ArticleInfoBean;
import com.soft.mydemo.bean.RespBean;
import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.service.ArticleService;
import com.soft.mydemo.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by fmz200 on 2021/08/07
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    // 上传路径
    @Value("${upload.blog}")
    private String blog;

    @Value("${upload.blogImgMapper}")
    private String blogImgMapper;

    @Autowired
    ArticleService articleService;

    @PostMapping(value = "/")
    public RespBean addNewArticle(ArticleInfoBean article) {
        int result = articleService.addNewArticle(article);
        if (StringUtils.equals(CommonConstants.CODE_SUCCESS, result)) {
            return new RespBean(CommonConstants.SUCCESS, article.getId() + "");
        } else {
            return new RespBean(CommonConstants.ERROR, article.getState() == 0 ? "文章保存失败!" : "文章发表失败!");
        }
    }

    /**
     * 上传图片
     *
     * @return 返回值为图片的地址
     */
    @PostMapping(value = "/uploadimg")
    public RespBean uploadImg(HttpServletRequest req, MultipartFile image) {
        StringBuilder url = new StringBuilder();
        String dateString = TimeUtils.getCurrentDateString();
        String filePath = blog + dateString;
        File imgFolder = new File(filePath);
        if (!imgFolder.exists()) {
            boolean mkdirs = imgFolder.mkdirs();
            log.info("mkdirs is {}", mkdirs);
        }

        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        log.debug("serverName is {},serverPort is {}", serverName, serverPort);
        url.append(req.getScheme())
                .append("://")
                .append(serverName)
                .append(":")
                .append(serverPort);
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            // 虚拟映射路径
            String imgPath = String.valueOf(url.append(blogImgMapper).append(dateString).append("/").append(imgName));
            log.info("update imgPath is {}", imgPath);
            return new RespBean(CommonConstants.SUCCESS, imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RespBean(CommonConstants.ERROR, "上传失败!");
    }

    @RequestMapping(value = "/all")
    public Map<String, Object> getArticleByState(ArticleInfoBean params) {
        log.error(blog);
        log.info("article.all start... params is {}", JSON.toJSONString(params));
        return articleService.getArticleByState(params);
    }

    @GetMapping(value = "/{aid}")
    public ArticleInfoBean getArticleById(@PathVariable Long aid) {
        return articleService.getArticleById(aid);
    }

    @PutMapping(value = "/dustbin")
    public RespBean updateArticleState(Long[] aids, Integer state) {
        if (articleService.updateArticleState(aids, state) == aids.length) {
            return new RespBean(CommonConstants.SUCCESS, "删除成功!");
        }
        return new RespBean(CommonConstants.ERROR, "删除失败!");
    }

    @PutMapping(value = "/restore")
    public RespBean restoreArticle(Integer articleId) {
        if (articleService.restoreArticle(articleId) == 1) {
            return new RespBean(CommonConstants.SUCCESS, "还原成功!");
        }
        return new RespBean(CommonConstants.ERROR, "还原失败!");
    }

    @RequestMapping("/dataStatistics")
    public Map<String, Object> dataStatistics() {
        Map<String, Object> map = new HashMap<>();
        List<String> categories = articleService.getCategories();
        List<Integer> dataStatistics = articleService.getDataStatistics();
        map.put("categories", categories);
        map.put("ds", dataStatistics);
        return map;
    }
}
