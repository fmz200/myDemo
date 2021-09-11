package com.soft.mydemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Locale;

public class FileUtils {

    //定义一个全局的记录器，通过LoggerFactory获取
    protected static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    // 单次传送最大字节数20M。
    private final static int maxsize_once;

    static {
        maxsize_once = 1024 * 1024 * 20;
    }

    /**
     * @param name         文件名字
     * @param downloadPath 文件下载路径
     */
    public static void download(String flag, String name, String downloadPath, HttpServletResponse resp) {
        BufferedInputStream bis = null;
        OutputStream os = null;
        InputStream ins;
        File file;
        long lengths;
        try {
            if (flag.equals("1")) {
                ClassPathResource classPathResource = new ClassPathResource(downloadPath);
                lengths = classPathResource.contentLength();
                ins = classPathResource.getInputStream();
            } else {
                file = new File(downloadPath);
                lengths = file.length();
                ins = new FileInputStream(file);
            }

            if (downloadPath.startsWith("template/import")) {
                int sufix = downloadPath.indexOf(".");
                name += downloadPath.substring(sufix);
            }

            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("utf-8");
            resp.setContentLengthLong(lengths);
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));

            byte[] buff = new byte[1024];

            os = resp.getOutputStream();
            bis = new BufferedInputStream(ins);
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(os, bis);
        }
    }

    /**
     * 返回流
     *
     * @param downloadPath
     */
    public static byte[] downloadIO(String downloadPath) {
        byte[] filebyte = null;

        // 读取文件字节流
        ByteArrayOutputStream fileStream = new ByteArrayOutputStream(1024);
        FileInputStream file = null;
        try {
            file = new FileInputStream(downloadPath);
            int len = 0;
            byte[] readbuff = new byte[1024];
            while ((len = file.read(readbuff, 0, readbuff.length)) != -1) {
                fileStream.write(readbuff, 0, len);
            }
            // 构建返回文件字节信息。超过20M, 一次只返回20M。
//	        final byte[] fileBuff = fileStream.toByteArray();

//	        filebyte = Arrays.copyOfRange(fileBuff, 0, fileBuff.length);
            filebyte = fileStream.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(file, fileStream);
        }
        return filebyte;
    }

}
