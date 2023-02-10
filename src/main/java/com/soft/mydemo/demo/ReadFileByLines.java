package com.soft.mydemo.demo;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ReadFileByLines {

    /**
     * quanX 去除重复的hostname
     */
    @Test
    public void readFileByLines() {
        String fileName = "/Users/king/project/myDemo/src/main/java/com/soft/mydemo/demo/hostname.txt";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("第 " + line + " 行内容: " + tempString);
                line++;

                tempString = tempString.replaceAll(" ", "");
                List<String> stringList = Arrays.asList(tempString.split(","));
                System.out.println("去重前个数：" + stringList.size());
                HashSet<String> set = new HashSet<>(stringList);
                System.out.println("去重后个数：" + set.size());
                String result_string = StringUtils.join(set.toArray(), ", ");
                System.out.println("去重后：" + result_string);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    // 整理格式为“注释+脚本+空行”的格式
    @Test
    public void readFileByLines1() {
        String fileName = "/Users/king/project/myDemo/src/main/java/com/soft/mydemo/demo/unlock.txt";
        String outName = "/Users/king/project/myDemo/src/main/java/com/soft/mydemo/demo/unlock_out.txt";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行");
            reader = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outName));
            String temp;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((temp = reader.readLine()) != null) {
                // 显示行号
                System.out.println("第 " + line + " 行内容: " + temp);
                line++;
                if (StringUtils.isNotEmpty(temp)) {
                    if (temp.startsWith("https")) {
                        // 写入一行
                        bw.write(temp + "\n\n");
                    } else {
                        bw.write("# " + temp + "\n");
                    }
                }
            }
            System.out.println("去重后：");
            reader.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
