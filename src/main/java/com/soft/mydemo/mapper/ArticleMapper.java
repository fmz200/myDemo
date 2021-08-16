package com.soft.mydemo.mapper;

import com.soft.mydemo.bean.ArticleInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by fmz200 on 2021/08/07
 */
@Mapper
public interface ArticleMapper {
    int addNewArticle(ArticleInfoBean article);

    int updateArticle(ArticleInfoBean article);

    List<ArticleInfoBean> getArticleByState(@Param("state") Integer state, @Param("uid") Long uid,
                                            @Param("keywords") String keywords);

    List<ArticleInfoBean> getArticleByStateByAdmin(@Param("start") int start, @Param("count") Integer count, @Param("keywords") String keywords);

    int getArticleCountByState(@Param("state") Integer state, @Param("uid") Long uid, @Param("keywords") String keywords);

    int updateArticleState(@Param("aids") Long[] aids, @Param("state") Integer state);

    int updateArticleStateById(@Param("articleId") Integer articleId, @Param("state") Integer state);

    int deleteArticleById(@Param("aids") Long[] aids);

    ArticleInfoBean getArticleById(Long aid);

    void pvIncrement(Long aid);

    //INSERT INTO pv(countDate,pv,uid) SELECT NOW(),SUM(pageView),uid FROM article GROUP BY uid
    void pvStatisticsPerDay();

    List<String> getCategories(Long uid);

    List<Integer> getDataStatistics(Long uid);
}
