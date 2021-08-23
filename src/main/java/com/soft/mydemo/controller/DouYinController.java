package com.soft.mydemo.controller;

import com.soft.mydemo.bean.douyin.DouYinBean;
import com.soft.mydemo.bean.douyin.DouYinResult;
import com.soft.mydemo.util.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 抖音去水印，copy自互联网
 *
 * @author admin
 */
@Slf4j
@Controller
@RequestMapping(value = "/douyin")
public class DouYinController {

    public static String DOU_YIN_BASE_URL = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    public static void main(String[] args) throws Exception {
        String url = "https://v.douyin.com/dRPkaeu/";
        parseVideoUrl(url);
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
    public static String parseVideoUrl(@RequestBody String url) throws Exception {
        DouYinBean douYinBean = new DouYinBean();
        try {
            url = URLDecoder.decode(url).replace("url=", "");
            // 1、短连接重定向后的 URL
            String redirectUrl = getLocation(url);
            log.info("parseVideoUrl.redirectUrl is {}", redirectUrl);
            if (StringUtils.isEmpty(redirectUrl)) {
                return null;
            }
//10:42:35.615 [main] INFO com.soft.mydemo.controller.DouYinController - parseVideoUrl.redirectUrl is https://www.iesdouyin.com/share/video/6999205120957336865/?region=CN&mid=6924587721335868168&u_code=157eekbkm&titleType=title&did=MS4wLjABAAAAgFKWoW0AuTx7invBWeJaK3PWyShtR0UPtYPl-3_YILRQ94b0fhsDNrtphXYlfe7i&iid=MS4wLjABAAAA6d-qTxAXfRMtCkfrc0njkKAqi2ONmvGwFxmTpBB2BgsfEH75RaQl8BZMmiOq28W6&with_sec_did=1&timestamp=1629686338&utm_campaign=client_share&app=aweme&utm_medium=ios&tt_from=copy&utm_source=copy
            // 2、拿到视频对应的 ItemId
            String itemId = ""; //CommonUtils.matchNo(redirectUrl);

            // 3、用 ItemId 拿视频的详细信息，包括无水印视频url
            StringBuilder sb = new StringBuilder();
            sb.append(DOU_YIN_BASE_URL).append(itemId);
            String videoResult = HttpClientUtil.doGet(sb.toString());
            DouYinResult dyResult = JSON.parseObject(videoResult, DouYinResult.class);

            // 4、无水印视频 url
            String videoUrl = dyResult.getItem_list().get(0)
                    .getVideo().getPlay_addr().getUrl_list().get(0)
                    .replace("playwm", "play");
            String videoRedirectUrl = getLocation(videoUrl);
            douYinBean.setVideoUrl(videoRedirectUrl);
            /**
             * 5、音频 url
             */
            String musicUrl = dyResult.getItem_list().get(0).getMusic().getPlay_url().getUri();
            douYinBean.setMusicUrl(musicUrl);
            /**
             * 6、封面
             */
            String videoPic = dyResult.getItem_list().get(0).getVideo().getDynamic_cover().getUrl_list().get(0);
            douYinBean.setVideoPic(videoPic);
            /**
             * 7、视频文案
             */
            String desc = dyResult.getItem_list().get(0).getDesc();
            douYinBean.setDesc(desc);
        } catch (Exception e) {
            log.error("parseVideoUrl has an error...", e);
        }
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
            String location = conn.getHeaderField("Location");
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
