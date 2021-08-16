package com.soft.mydemo.util;

/**
 * 通用工具类
 *
 * @author wpenghui
 * @date 2020年1月4日
 */
public class CommonsUtils {

    /**
     * 切分数据校验，code中是否包含data的值
     *
     * @param code 原数据
     * @param data 待校验的值
     * @return
     */
    public static boolean splitForData(String code, String data) {
        boolean result = false;

        String[] str = code.split(",");
        for (String s : str) {
            if (data.equals(s)) {
                result = true;
            }
        }
        return result;
    }

}
