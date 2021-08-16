package com.soft.mydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by fmz200 on 2021/08/07
 */
@Component
public class DataStatisticsComponent {
    @Autowired
    ArticleService articleService;

    //每天执行一次，统计PV
    @Scheduled(cron = "1 0 0 * * ?")
    public void pvStatisticsPerDay() {
        articleService.pvStatisticsPerDay();
    }
}
