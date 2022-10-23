package com.soft.mydemo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageUtils {

    /**
     * 检查字符串是否包含中文字符
     * 1、针对每个字符判断
     * 2、优缺点：
     * a.缺点：效率低【每次都需要循环检测字符串中每个字符】（每次发送都需要检测短信内容，每条内容有很多字符）；
     * b.优点：不仅能检测出中文汉字还能检测中中文标点；
     */
    public static boolean isChinese(String str) throws UnsupportedEncodingException {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            String temp = URLEncoder.encode(str.charAt(i) + "", StandardCharsets.UTF_8);
            if (temp.equals(str.charAt(i) + ""))
                continue;
            String[] codes = temp.split("%");
            // 判断是中文还是字符(下面判断不精确，部分字符没有包括)
            for (String code : codes) {
                if (code.compareTo("40") > 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * 检查字符串是否包含中文字符
     * 1、利用正则表达式
     * 2、优缺点：
     * a.缺点：只能检测出中文汉字不能检测中文标点；
     * b.优点：利用正则效率高；
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 检查字符串是否包含中文字符
     * 1、改造正则
     * 2、优缺点：
     * a.缺点：效率既高又能检测出中文汉字和中文标点；
     * b.优点：目前尚未发现。
     */
    public static boolean isContainChineseAnd(String str)  {
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
