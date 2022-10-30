package com.soft.mydemo.demo;

import com.soft.mydemo.util.LanguageUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 读取目录下的每个文件和文件夹，并读取文件的内容写到目标文件中去
 */
public class FileCombine {

    @Test
    public void fileCombine() throws IOException {
        // 要合并的文件所在的文件夹
        String fileInFolder = "/Users/king/project/myDemo/src/main/java/com/soft/mydemo/demo/89996462";
        // 定义输出文件
        String fileOut = "/Users/king/project/myDemo/src/main/java/com/soft/mydemo/demo/rewrite_unlockVIP.snippet";

        List<File> myFiles = new ArrayList<>();
        getFiles(new File(fileInFolder), myFiles);
        System.out.println("输入目录下文件个数为：" + myFiles.size());
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
        bw.write("# 重写规则配置片段包含若干条重写规则，并可以包含若干作用于 MitM 的主机名；可通过资源引用的方式使用。");
        bw.newLine();
        bw.write("; hostname line is optional.");
        bw.newLine();
        bw.write("# 片段文件将保存在 Quantumult X 目录下的 Profiles 子目录中。");
        bw.newLine();
        bw.write("# 样例可参见 https://raw.githubusercontent.com/crossutility/Quantumult-X/master/sample-import-rewrite.snippet");
        bw.newLine();
        bw.newLine();
        bw.newLine();

        List<String> listName = Arrays.asList("作者", "问题", "TG", "声明", "QQ");
        String hostname = "";
        for (File file : myFiles) {
            // String path = "文件路径：" + file.getPath().substring(file.getPath().indexOf("-main") + 5);
            String path = "文件名：" + file.getName();
            System.out.println(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            boolean write = true;

            // 每个文件进来先写文件路径或文件名
            bw.write("# " + path);
            bw.newLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && !containsWords(listName, line) && write) {
                    // 如果包含中文，则是注释
                    if (LanguageUtils.isContainChinese(line)) {
                        // 写一行内容并换行
                        bw.write("# " + line);
                        bw.newLine();
                    }

                    // 读取 rewrite_local
                    if (line.contains("raw.githubusercontent.com")) {
                        bw.write(line);
                        bw.newLine();
                    }
                    if (line.contains("hostname")) {
                        System.out.println(line);
                        hostname = hostname.concat(line.replaceAll(" ", "").replace("hostname=", "")).concat(",");
                        write = false; // 读取到hostname标识停止写入
                    }
                }
            }
            bw.newLine();
            br.close();
        }
        //  hostname=restore-access.indream.app
        HashSet<String> set = new HashSet<>(Arrays.asList(hostname.split(",")));
        set.remove("*");
        set.remove("api.*.com");
        String str1 = StringUtils.join(set.toArray(), ", ");
        bw.write("hostname = " + str1);
        bw.close();
        System.out.println("结束：" + myFiles.size());
    }

    public static boolean containsWords(List<String> listName, String word) {
        for (String key : listName) {
            if (word.contains(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定目录及其子目录下的所有的文件，采用了递归
     */
    public static void getFiles(File path, List<File> myFiles) {
        if (path.exists()) {
            if (path.isFile()) {
                myFiles.add(path);
            } else {
                File[] files = path.listFiles();
                assert files != null;
                for (File file : files) {
                    getFiles(file, myFiles);
                }
            }
        }
    }

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

}
