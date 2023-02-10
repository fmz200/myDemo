package com.soft.mydemo.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;


public class DownloadFileFromURL {

    /**
     * 下载爱思助手专题里面的壁纸合集
     */
    @Test
    public void downloadFileFromURL() {
        String fileName = "/Users/king/Project/myDemo/src/main/java/com/soft/mydemo/demo/file/爱思-点燃我温暖你壁纸链接.json";
        String fileOutPath = "/Users/king/Project/myDemo/src/main/java/com/soft/mydemo/demo/file/";
        File file = new File(fileName);
        try {
            JSONObject jsonObject = nioMethod(file);
            String topicName = jsonObject.getJSONObject("specialtitle").getString("name");
            JSONArray list = jsonObject.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String id = list.getJSONObject(i).getString("id");
                String url = list.getJSONObject(i).getString("largeurl");
                String filePath = fileOutPath.concat(topicName).concat("-").concat(String.valueOf(i)).concat(url.substring(url.lastIndexOf(".")));
                download(url, filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject nioMethod(File file) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(file.getPath())));
        return JSON.parseObject(jsonString);
    }

    public static String download(String urlString, String fileOutPath) throws IOException {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            String property = System.getProperty("os.name");
            String filename = System.getProperty("os.name").toLowerCase().contains("win") ?
                    System.getProperty("user.home") + "\\Desktop\\temp.jpg" : "/home/project/mkhr_applets_2.0/temp.jpg";
            File file = new File(fileOutPath);
            os = new FileOutputStream(file, true);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            return fileOutPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //从微信下载图片时如果没有id对应的图片则下载一个空图片，不会存在返回为null的情况
        return null;
    }
}
