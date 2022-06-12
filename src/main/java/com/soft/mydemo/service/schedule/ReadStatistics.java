package com.soft.mydemo.service.schedule;

import com.soft.mydemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 统计每天的阅读数据
 * Created by fmz200 on 2021/08/07
 */
@Component
@EnableScheduling
public class ReadStatistics {
    @Autowired
    ArticleService articleService;

    //每天执行一次，统计PV
    @Scheduled(cron = "1 0 0 * * ?")
    public void pvStatisticsPerDay() {
        articleService.pvStatisticsPerDay();
    }
}
