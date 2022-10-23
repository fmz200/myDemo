package com.soft.mydemo.test;

import com.soft.mydemo.utils.FileDownloadUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTest {
    public static void main(String[] args) throws IOException {
        String path = "/Users/king/project/myDemo/src/main/webapp/flag.html";
        //BufferedReader是可以按行读取文件
        FileInputStream inputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        HashSet<String> set = new HashSet<>();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            String line = str.trim();
            System.out.println(line);
            // src="//00.ifreesite.com/world-i/japan_flag.png" title="Japan - 日本" width="81"
            if (line.contains("//00.ifreesite.com/world-i")) {
                // 第一个 " 开始截取。第二个 " 结束
                int first = line.indexOf("\"");
                int last = getIndexOf(line, "\"", 2);
                String substring = line.substring(first + 1, last);
                // https://00.ifreesite.com/world-i/china_flag.png
                set.add("https:" + substring);
            }
        }

        for (String s : set) {
            System.err.println(s);
            String fileName = s.substring(s.lastIndexOf("/") + 1);
            // /Users/king/Downloads/世界各地区旗帜
            String saveFile = "/Users/king/Downloads/世界各地区旗帜/" + fileName;
            FileDownloadUtils.httpDownload(s, saveFile);
        }
        //close
        inputStream.close();
        bufferedReader.close();

    }

    // //获取某个字符在指定字符串中出现的第N次的位置
    public static int getIndexOf(String data, String str, int num) {
        Pattern pattern = Pattern.compile(str);
        Matcher findMatcher = pattern.matcher(data);
        //标记遍历字符串的位置
        int indexNum = 0;
        while (findMatcher.find()) {
            indexNum++;
            if (indexNum == num) {
                break;
            }
        }
        return findMatcher.start();
    }
}
