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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;

public class FileUtils {
    // 单次传送最大字节数20M。
    private  static final int MAXSIZE_ONCE = 1024 * 1024 * 20;

    /**
     * @param name         文件名字
     * @param downloadPath 文件下载路径
     * @param resp
     */
    public static void download(String flag, String name, String downloadPath, HttpServletResponse resp) {
        BufferedInputStream bis;
        OutputStream os;
        InputStream ins;
        File file;
        Long lengths;
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
                int suffix = downloadPath.indexOf(".");
                name += downloadPath.substring(suffix);
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
            //IOUtil.closeQuietly(os,bis);
        }
    }

    /**
     * 返回流
     *
     * @param downloadPath 文件路径
     */
    public static byte[] downloadIO(String downloadPath) {
        byte[] filebyte = null;

        // 读取文件字节流
        ByteArrayOutputStream fileStream = new ByteArrayOutputStream(1024);
        try {
            FileInputStream file = new FileInputStream(downloadPath);
            int len = 0;
            byte[] readbuff = new byte[1024];
            while ((len = file.read(readbuff, 0, readbuff.length)) != -1) {
                fileStream.write(readbuff, 0, len);
            }
            filebyte = fileStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //IOUtil.closeQuietly(file,fileStream);
        }
        return filebyte;
    }

    public static String getFileSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        double value = size;
        if (value < 1024) {
            return value + "B";
        } else {
            value = BigDecimal.valueOf(value / 1024).setScale(2, RoundingMode.DOWN).doubleValue();
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (value < 1024) {
            return value + "KB";
        } else {
            value = BigDecimal.valueOf(value / 1024).setScale(2, RoundingMode.DOWN).doubleValue();
        }
        if (value < 1024) {
            return value + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            value = BigDecimal.valueOf(value / 1024).setScale(2, RoundingMode.DOWN).doubleValue();
            return value + "GB";
        }
    }

}
