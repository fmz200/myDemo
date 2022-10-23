package com.soft.mydemo.util;

import com.alibaba.fastjson2.JSONObject;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageUtilsTest {
    // 1.通过 url 获取图片并调整大小后保存
    @Test
    public void test01() {
        try {
            //通过url获取BufferedImage图像缓冲区
            URL img = new URL("https://img1.360buyimg.com/image/jfs/t1/38591/20/3737/314695/5cc69c01E1838df09/dd6dce681bd23031.jpg");
            BufferedImage image = ImageIO.read(img);
            //获取图片的宽、高
            System.out.println("Width: " + image.getWidth());
            System.out.println("Height: " + image.getHeight());
            //调整图片大小为 400X400尺寸
            BufferedImage newImage = ImageUtils.resizeImage(image, 400, 400);
            //将缓冲区图片保存到 F:/test/pic1.jpg (文件不存在会自动创建文件保存，文件存在会覆盖原文件保存)
            ImageIO.write(newImage, "jpg", new File("F:/test/pic1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2.通过读取图片文件调整大小再保存
    @Test
    public void test02() {
        try {
            //读取原始图片
            BufferedImage image = ImageIO.read(new FileInputStream("F:/test/pic1.jpg"));
            System.out.println("Width: " + image.getWidth());
            System.out.println("Height: " + image.getHeight());
            //调整图片大小
            BufferedImage newImage = ImageUtils.resizeImage(image, 200, 200);
            //图像缓冲区图片保存为图片文件(文件不存在会自动创建文件保存，文件存在会覆盖原文件保存)
            ImageIO.write(newImage, "jpg", new File("F:/test/pic2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 3.MultipartFile类型的图片文件调整大小再保存
    public JSONObject imageSizeAdjustment(MultipartFile file) {
        JSONObject result = new JSONObject();
        try {
            //从MultipartFile 中获取 byte[]
            byte[] bytes = file.getBytes();
            //byte[]转 InputStream
            InputStream in = new ByteArrayInputStream(bytes);
            //读取图片输入流为 BufferedImage
            BufferedImage image = ImageIO.read(in);
            System.out.println("Width: " + image.getWidth());
            System.out.println("Height: " + image.getHeight());
            //调整图片大小
            BufferedImage newImage = ImageUtils.resizeImage(image, 200, 200);
            //图像缓冲区图片保存为图片文件(文件不存在会自动创建文件保存，文件存在会覆盖原文件保存)
            ImageIO.write(newImage, "jpg", new File("F:/test/pic2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("code", 1);
        result.put("note", "成功");
        return result;
    }

    /**
     * 获取指定目录下的所有的文件
     * @param path
     * @return
     */
    public static List<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(tempList).length; i++) {
            File fs = tempList[i];
            if (fs.isFile()) {
                System.out.println("文件：" + fs);
                // files.add(fs.getName()); // 只获取名字
                files.add(fs.toString()); // 获取完整的文件路径
            }
            if (fs.isDirectory()) {
                System.out.println("文件夹：" + fs);
            }
        }
        return files;
    }

    /**
     * 改变图片文件大小
     */
    @Test
    public void changeImageSize() {
        // /Users/king/Downloads/LPL战队logo/002 读取不到这个目录
        // /Users/king/project/icons/regionFlag/rf
        // /Users/king/project/icons/LPLTeam/002
        String path = "/Users/king/project/icons/LPLTeam/002";
        List<String> files = getFiles(path);
        int size = 144;
        for (String filePath : files) {
            try {
                // 读取原始图片
                BufferedImage image = ImageIO.read(new FileInputStream(filePath));
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                System.out.println("文件名：" + fileName + "，Width: " + image.getWidth() + "，Height: " + image.getHeight());
                // 调整图片大小
                BufferedImage newImage = ThumbnailsUtils.resizeImageOne(image, size, size);
                // 缓冲区图片保存为图片文件(文件不存在会自动创建文件保存，文件存在会覆盖原文件保存)
                ImageIO.write(newImage, "png", new File(path + "/" + size + "/" + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 生成json文件
     */
    @Test
    public void buildJsonData() {
        JSONObject json = new JSONObject();
        json.put("name", "世界各地区旗帜 icon");
        json.put("description", "搜索要输入英文全名，例如China");
        // https://raw.githubusercontent.com/fmz200/icons/master/LPLTeam/LPLTeam.json
        // https://raw.githubusercontent.com/fmz200/icons/master/LPLTeam/icons/AL.png
        List<JSONObject> teamPicLink = new ArrayList<>();
        JSONObject icon;

        List<String> files = getFiles("/Users/king/project/icons/regionFlag/rf144");
        for (String name : files) {
            icon = new JSONObject();
            System.out.println("遍历文件：" + name);
            icon.put("name", name.substring(0, name.indexOf("flag") - 1));
            icon.put("url", "https://raw.githubusercontent.com/fmz200/icons/master/regionFlag/rf144/" + name);
            teamPicLink.add(icon);
        }
        json.put("icons", teamPicLink);
        System.out.println(json);
    }

}
