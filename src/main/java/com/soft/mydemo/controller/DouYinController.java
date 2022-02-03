package com.soft.mydemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soft.mydemo.bean.douyin.DouYinBean;
import com.soft.mydemo.bean.douyin.DouYinResult;
import com.soft.mydemo.util.HttpRequest;
import com.soft.mydemo.utils.FileDownloadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抖音去水印，copy自互联网
 *
 * @author admin
 */
@Slf4j
@Controller
@RequestMapping(value = "/douYin")
public class DouYinController {

    public static final String DOU_YIN_BASE_URL = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    // 抖音热搜url
    public static final String HOT_SEARCH_DOU_YIN = "https://creator.douyin.com/aweme/v1/creator/data/billboard/?billboard_type=1";

    @ResponseBody
    @RequestMapping(value = "hotSearch")
    public JSONObject hotSearch() {
        String getResult = HttpRequest.sendGet(HOT_SEARCH_DOU_YIN, "");
        log.info("hotSearch.getResult is {}", getResult);
        return JSON.parseObject(getResult);
    }

    /**
     * 解析抖音无水印视频
     *
     * @param url 抖音复制的分享链接
     * @author admin
     * @date 2021/9/15
     */
    @RequestMapping("/parseVideoUrl")
    @ResponseBody
    public static String parseVideoUrl(String url, HttpServletResponse response) throws Exception {
        log.info("parseVideoUrl.url is {}", url);
        if (StringUtils.isEmptyOrWhitespace(url)) {
            return null;
        }
        // 过滤链接，获取视频连接地址
        url = decodeDyUrl(url);
        DouYinBean douYinBean = new DouYinBean();
        try {
            url = URLDecoder.decode(url, "UTF-8").replace("url=", "");
            // 1、短连接重定向后的 URL
            // https://www.iesdouyin.com/share/video/6999205120957336865/?region=CN&mid=6924587721335868168...
            String redirectUrl = getLocation(url);
            log.info("parseVideoUrl.redirectUrl is {}", redirectUrl);
            if (StringUtils.isEmptyOrWhitespace(redirectUrl)) {
                return null;
            }

            // 2、拿到视频对应的 ItemId
            String itemId = matchItemId(redirectUrl);
            log.info("parseVideoUrl.itemId is {}", itemId);

            // 3、用 ItemId 拿视频的详细信息，包括无水印视频url
            // https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=7045134641002466590
            String videoResult = HttpRequest.sendGet(DOU_YIN_BASE_URL + itemId, "");
            log.info("parseVideoUrl.videoResult is {}", videoResult);
            DouYinResult dyResult = JSON.parseObject(videoResult, DouYinResult.class);
            if (ObjectUtils.isEmpty(dyResult) || CollectionUtils.isEmpty(dyResult.getItem_list())) {
                return null;
            }

            // 4、无水印视频 url
            String videoUrl = dyResult.getItem_list().get(0).getVideo().getPlay_addr().getUrl_list().get(0).replace("playwm", "play");
            douYinBean.setVideoUrl(videoUrl);
            // 4.1 下载抖音视频到本地指定的文件夹
            boolean b = FileDownloadUtils.httpDownload(videoUrl, "D:\\home\\02.mp4");
            // 4.2 下载抖音视频到浏览器页面
            // DownloadUtils.download("0", itemId, videoUrl, response);

            log.info("parseVideoUrl.b is {}", b);

            // 5、音频 url
            String musicUrl = dyResult.getItem_list().get(0).getMusic().getPlay_url().getUri();
            douYinBean.setMusicUrl(musicUrl);

            // 6、封面
            String videoPic = dyResult.getItem_list().get(0).getVideo().getDynamic_cover().getUrl_list().get(0);
            douYinBean.setVideoPic(videoPic);

            //7、视频文案
            String desc = dyResult.getItem_list().get(0).getDesc();
            douYinBean.setDesc(desc);
        } catch (Exception e) {
            log.error("parseVideoUrl has an error...", e);
        }
        log.info("parseVideoUrl douYinResult is {}", douYinBean);
        return JSON.toJSONString(douYinBean);
    }

    /**
     * 获取当前链接重定向后的url
     *
     * @param url 抖音视频链接
     * @author admin
     * @date 2021/9/15
     */
    public static String getLocation(String url) {
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("User-agent", "ua"); // 模拟手机连接
            conn.connect();
            return conn.getHeaderField("Location");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 方法2
     */
    public void dy() {
        try {
            String url1 = "#在抖音，记录美好生活# https://v.douyin.com/qsSFEV/ 复制此链接，打开【抖音短视频】，直接观看视频！";

            //过滤链接，获取视频连接地址
            String dyUrl = decodeDyUrl(url1);

            URL url = new URL(dyUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            int code = conn.getResponseCode();
            if (302 == code) {
                url1 = conn.getHeaderField("Location");
            }
            conn.disconnect();

            String videoId;
            int start = url1.indexOf("/?");
            int end = url1.lastIndexOf("/", start - 1);
            videoId = url1.substring(end + 1, start);
            dyUrl = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=" + videoId;

            //抓取抖音网页
            String html = Jsoup.connect(dyUrl).ignoreContentType(true).execute().body();

            //利用正则表达式获取视频链接
            Pattern patternCompile = Pattern.compile("(?<=\")[^\"]+playwm[^\"]+(?=\")");
            //3.匹配后封装成Matcher对象
            Matcher m = patternCompile.matcher(html);

            //4.①利用Matcher中的group方法获取匹配的特定字符串 ②利用String的replace方法替换特定字符,得到抖音的去水印链接
            String matchUrl = "";
            while (m.find()) {
                matchUrl = m.group(0).replaceAll("playwm", "play");
            }

            if (TextUtils.isEmpty(matchUrl)) {
                //解析失败
                log.error("error");
            } else {
                //matchUrl就是去水印的视频地址 你想干啥就干啥吧
                log.info("success {}",matchUrl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decodeDyUrl(String url) {
        int start = url.indexOf("http");
        int end = url.lastIndexOf("/");
        return url.substring(start, end);
    }

    public static String matchItemId(String url) {
        // eg:https://www.iesdouyin.com/share/video/6999205120957336865/?region=CN&mid=6924587721335868168...
        int video = url.lastIndexOf("video");
        int region = url.lastIndexOf("region");
        return url.substring(video + 6, region - 2);
    }
}
