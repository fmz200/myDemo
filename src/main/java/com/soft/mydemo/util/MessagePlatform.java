package com.soft.mydemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MessagePlatform {

    private final Logger logger = LoggerFactory.getLogger(MessagePlatform.class);

    public void creatPostAndTransData(String xmlInfo) {
        // 声明URL
        String username = "SMI";
        String password = "Pl#smi!0909";
        String usernameMd5 = getMd5(username);
        String passwordMd5 = getMd5(password);

        // 测试地址
        String urlStr = "";
        String line = "";
        try {
            logger.info(">>>>>>>>>>接口地址为urlStr=" + urlStr);
            logger.info(">>>>>>>>>>传入的报文xmlInfo=" + xmlInfo);
            byte[] xmlData = xmlInfo.getBytes("GBK");
            InputStream fi = new ByteArrayInputStream(xmlData);
            URL jspUrl = new URL(urlStr);
            // 创建链接
            URLConnection uc = jspUrl.openConnection();
            uc.setDoOutput(true);
            OutputStream os = uc.getOutputStream(); //得到客户端的输出流对象
            int length = 0;
            byte[] buffer = new byte[4096];
            while ((length = fi.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            os.flush(); //将输出流提交到服务器端
            os.close(); //关闭输出流对象
            // 获取返回报文
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    uc.getInputStream(), "GBK"));
            //对返回值报文进行打印
            for (line = br.readLine(); line != null; line = br.readLine()) {
                //对返回的报文进行结果判断<RspCode>0000</RspCode>
                logger.info(">>>>>>>>>>>>>>>>>>>返回的结果报文内容为:---------" + line);
                //对返回的报文进行拼接,然后返回给业务层,在业务层进行判断
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMd5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }
            byte[] md5Bytes = md.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (byte md5Byte : md5Bytes) {
                int val = (int) md5Byte & 0xff;
                if (val < 16) {
                    hexValue.append(0);
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

}
