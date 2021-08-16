package com.soft.mydemo.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pdf2Image {

    //定义一个全局的记录器，通过LoggerFactory获取
    protected static Logger logger = LoggerFactory.getLogger(Pdf2Image.class);

    public static int convert(String path, String sourceFileName, String targetFileName) throws IOException {
        int count = 0;
        long starttime = System.currentTimeMillis();
        logger.info("开始执行图片转换");
        String sourceFilePath = path + sourceFileName;
        String name = targetFileName.substring(0, targetFileName.lastIndexOf("."));
        String surfix = targetFileName.substring(targetFileName.lastIndexOf("."));
        String targetFile = path + name;
        File file = new File(sourceFilePath);

        PDDocument doc = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(doc);
        int pageCount = doc.getNumberOfPages();
        for (int i = 0; i < pageCount; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, 296);
            if (i < 10) {
                ImageIO.write(image, "PNG", new File(targetFile + "(0" + i + ")" + surfix));
            } else {
                ImageIO.write(image, "PNG", new File(targetFile + "(" + i + ")" + surfix));
            }
            count++;
        }
        long endtime = System.currentTimeMillis();
        logger.info("结束执行图片转换,总耗时" + (endtime - starttime));
        return count;
    }
}
