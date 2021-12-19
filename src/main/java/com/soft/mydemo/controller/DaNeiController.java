package com.soft.mydemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.soft.mydemo.bean.SalesInfoBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author admin
 */
@Slf4j
@Controller
@RequestMapping(value = "/hello")
public class DaNeiController {

    public static void main(String[] args) throws Exception {
        String url = "http://code.tarena.com.cn";
        doPostJson(url, new JSONObject().toJSONString());
    }

    @ResponseBody
    @RequestMapping(value = "getDaNeiFiles")
    public void getDaNeiFiles(SalesInfoBean salesInfoBeanReq) {

    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            //Header header = new Header();


            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Host", "code.tarena.com.cn");
            httpPost.addHeader("Proxy-Connection", "keep-alive");
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Cache-Control", "max-age=0");
            httpPost.addHeader("Authorization", "Basic dGFyZW5hY29kZTpjb2RlXzIwMTM=");
            httpPost.addHeader("DNT", "1");
            httpPost.addHeader("Upgrade-Insecure-Requests", "1");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");
            httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity());// 返回json格式：
            log.debug("querySalesInfo end... salesInfoBeanResp is {}", response);
            //resultString = JSONObject.fromObject(result).toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }
}
