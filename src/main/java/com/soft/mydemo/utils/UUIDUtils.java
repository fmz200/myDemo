package com.soft.mydemo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UUIDUtils {
    public static String generateUID() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(new Date());
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            //首字母不能为0
            result.append(random.nextInt(9) + 1);
        }
        return date + result;
    }
}
