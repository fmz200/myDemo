package com.soft.mydemo.util;

import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class DownloadUtils {
    /**
     * 下载文件
     *
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

            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("utf-8");
            resp.setContentLengthLong(lengths);
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));

            byte[] buff = new byte[1024];

            os = resp.getOutputStream();
            bis = new BufferedInputStream(ins);
            int i;
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
     * @param downloadPath 文件路径
     */
    public static byte[] downloadIO(String downloadPath) {
        byte[] fileBytes = null;

        // 读取文件字节流
        ByteArrayOutputStream fileStream = new ByteArrayOutputStream(1024);
        FileInputStream file = null;
        try {
            file = new FileInputStream(downloadPath);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = file.read(bytes, 0, bytes.length)) != -1) {
                fileStream.write(bytes, 0, len);
            }
            fileBytes = fileStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(file, fileStream);
        }
        return fileBytes;
    }

}
