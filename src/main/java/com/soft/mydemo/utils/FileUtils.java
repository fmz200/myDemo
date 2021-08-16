package com.soft.mydemo.utils;

import com.soft.mydemo.common.CommonConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * 文件处理
 *
 * @author admin
 * @date 2021/08/05
 */
public class FileUtils {

    public static void main0(String[] args) {
        String s = "";
        StringBuffer s1 = new StringBuffer(s);
        int index;
        for (index = CommonConstants.FEED_LENGTH; index < s1.length(); index += 6) {
            s1.insert(index, '\n');
        }
        System.out.println(s + "每隔76个字符换行：");
        System.err.println(s1);
    }

    public static void main1(String[] args) {
        File f0 = new File("C:\\Users\\admin\\Desktop\\01.txt");
        if (!f0.exists()) {
            return;
        }
        File f1 = new File("C:\\Users\\admin\\Desktop\\01out.txt");
        try {
            FileReader fr = new FileReader(f0);
            FileWriter fw = new FileWriter(f1);
            int ch;
            int size = 0;
            while ((ch = fr.read()) != -1) {
                fw.write(ch);
                // 每隔76个字符换行
                if (++size % CommonConstants.FEED_LENGTH == 0) {
                    fw.write('\n');
                }
            }
            fr.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main2(String[] args) throws IOException {
        File srcFile = new File("d:\\test.txt");
        File outFile = new File("d:\\dest.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(srcFile));
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile));
        char[] buffer = new char[CommonConstants.FEED_LENGTH];
        while (reader.read(buffer) != -1) {
            System.out.println(buffer);
            writer.write(String.valueOf(buffer).trim() + "\r\n");
        }
        reader.close();
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入源目录：");
        String sourcePath = sc.nextLine();
        System.out.println("请输入新目录：");
        String path = sc.nextLine();

        copyDir(sourcePath, path);
    }

    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath); //文件名称列表
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            boolean mkdir = (new File(newPath)).mkdir();
            System.err.println("mkdir = " + mkdir);
        }

        assert filePath != null;
        for (String s : filePath) {
            if ((new File(oldPath + File.separator + s)).isDirectory()) {
                copyDir(oldPath + File.separator + s, newPath + File.separator + s);
            }

            if (new File(oldPath + File.separator + s).isFile()) {
                copyFile(oldPath + File.separator + s, newPath + File.separator + s);
            }
        }
    }

    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);

        byte[] buffer = new byte[2097152];

        while ((in.read(buffer)) != -1) {
            out.write(buffer);
        }
    }
}
