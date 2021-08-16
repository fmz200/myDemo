package com.soft.mydemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 文件工具类
 *
 * @author wpenghui
 * @date 2019年8月21日
 */
public class MultipartFileUtils {

    //定义一个全局的记录器，通过LoggerFactory获取
    protected static Logger logger = LoggerFactory.getLogger(MultipartFileUtils.class);

    /**
     * web端文件上传到本地
     *
     * @param mFile
     * @param fileName
     * @return
     */
    public static boolean fileUploadLocal(MultipartFile mFile, String fileName) {
        boolean status = false;
        File file = null;
        try {
            file = new File(fileName);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {// 如果文件不存在，则创建文件,写入第一行内容
                file.createNewFile();
            }

            mFile.transferTo(file);
            status = true;
            logger.info(fileName + "个文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(getExceptionText(e));
            logger.info(fileName + "个文件上传失败");
        }
        return status;
    }

    public static String getExceptionText(Exception e) {
        String text = "";
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        text = sw.toString();
        return text;
    }

    /**
     * 判断文件大小是否超过5M
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {

        double fileSize = 0;
        if ("B".equalsIgnoreCase(unit)) {
            fileSize = (double) len;
        } else if ("K".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1073741824;
        }
        return !(fileSize > size);
    }

    /**
     * 判断文件大小是否超过5M
     *
     * @param files
     * @param size  限制大小
     * @param unit  限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkSize(MultipartFile files, int size, String unit) {
        long fileSize = 0L;
		/*for (MultipartFile file : files) {
			fileSize += file.getSize();
		}*/
        fileSize += files.getSize();
        return checkFileSize(fileSize, size, unit);
    }

}
