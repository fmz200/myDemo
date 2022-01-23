package com.soft.mydemo.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soft.mydemo.bean.ArticleInfoBean;
import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.mapper.ArticleMapper;
import com.soft.mydemo.mapper.TagsMapper;
import com.soft.mydemo.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fmz200 on 2021/08/07
 */
@Slf4j
@Service
@Transactional
public class ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagsMapper tagsMapper;

    public int addNewArticle(ArticleInfoBean article) {
        //处理文章摘要
        if (StringUtils.isEmptyOrWhitespace(article.getSummary())) {
            //直接截取
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, Math.min(stripHtml.length(), 30)));
        }
        // 新增文章
        if (article.getId() == -1) {
            //添加操作
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (StringUtils.equals(CommonConstants.STATE_ARTICLE_PUBLISHED, article.getState())) {
                //设置发表日期
                article.setPublishDate(timestamp);
            }
            article.setEditTime(timestamp);
            //设置当前用户
            article.setUid(UserUtils.getCurrentUser().getId());
            int i = articleMapper.addNewArticle(article);
            //打标签
            if (StringUtils.equals(CommonConstants.CODE_FAIL, addTagsToArticle(article))) {
                return CommonConstants.CODE_FAIL;
            }
            return i;
        } else {
            // 修改文章
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (StringUtils.equals(CommonConstants.STATE_ARTICLE_DRAFT, article.getSource())
                    && StringUtils.equals(CommonConstants.STATE_ARTICLE_PUBLISHED, article.getState())) {
                //设置发表日期
                article.setPublishDate(timestamp);
            }
            //更新
            article.setEditTime(new Timestamp(System.currentTimeMillis()));
            int i = articleMapper.updateArticle(article);
            //修改标签
            if (StringUtils.equals(CommonConstants.CODE_FAIL, addTagsToArticle(article))) {
                return CommonConstants.CODE_FAIL;
            }
            return i;
        }
    }

    private int addTagsToArticle(ArticleInfoBean article) {
        Long aid = article.getId();
        String[] dynamicTags = article.getDynamicTags();
        if (CollectionUtils.isEmpty(Arrays.asList(dynamicTags))) {
            return 1;
        }
        //1.删除该文章目前所有的标签
        tagsMapper.deleteTagsByAid(aid);
        //2.将上传上来的标签全部存入数据库
        tagsMapper.saveTags(dynamicTags);
        //3.查询这些标签的id
        List<Long> tIds = tagsMapper.getTagsIdByTagName(dynamicTags);
        //4.重新给文章设置标签
        int i = tagsMapper.saveTags2ArticleTags(tIds, aid);
        return i == dynamicTags.length ? i : -1;
    }

    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    public Map<String, Object> getArticleByState(ArticleInfoBean params) {
        Long uid = UserUtils.getCurrentUser().getId();
        params.setUid(uid);
        PageHelper.startPage(params.getPage(), params.getCount());
        List<ArticleInfoBean> articles = articleMapper.getArticleByState(params);
        PageInfo<ArticleInfoBean> pageInfo = new PageInfo<>(articles);

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", pageInfo.getTotal());
        map.put("articles", articles);
        log.info("getArticleByState end... map is {}", map);
        return map;
    }

//    public List<Article> getArticleByStateByAdmin(Integer page, Integer count,String keywords) {
//        int start = (page - 1) * count;
//        return articleMapper.getArticleByStateByAdmin(start, count,keywords);
//    }

    public int getArticleCountByState(Integer state, Long uid, String keywords) {
        return articleMapper.getArticleCountByState(state, uid, keywords);
    }

    public int updateArticleState(Long[] aids, Integer state) {
        if (state == 2) {
            return articleMapper.deleteArticleById(aids);
        } else {
            return articleMapper.updateArticleState(aids, 2);//放入到回收站中
        }
    }

    public int restoreArticle(Integer articleId) {
        return articleMapper.updateArticleStateById(articleId, 1); // 从回收站还原在原处
    }

    public ArticleInfoBean getArticleById(Long aid) {
        ArticleInfoBean article = articleMapper.getArticleById(aid);
        articleMapper.pvIncrement(aid);
        return article;
    }

    public void pvStatisticsPerDay() {
        articleMapper.pvStatisticsPerDay();
    }

    /**
     * 获取最近七天的日期
     *
     * @return
     */
    public List<String> getCategories() {
        return articleMapper.getCategories(UserUtils.getCurrentUser().getId());
    }

    /**
     * 获取最近七天的数据
     *
     * @return
     */
    public List<Integer> getDataStatistics() {
        return articleMapper.getDataStatistics(UserUtils.getCurrentUser().getId());
    }
}
