package com.soft.mydemo.demo;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ConvertOracleToMysql {
    @Test
    public void readFileByLinesToNewFile() {
        String fileName = "";
        String outName = "";
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 1;
        try {
            // write string to file
            FileWriter writer = new FileWriter(outName);
            BufferedWriter bw = new BufferedWriter(writer);
            System.out.println("以行为单位读取文件内容，一次读一整行");
            reader = new BufferedReader(new FileReader(file));
            String temp;
            // 一次读入一行，直到读入null为文件结束
            while ((temp = reader.readLine()) != null) {
                // 显示行号
                System.out.println("读取第 " + line + " 行...");
                line++;
                // 显示行号
                if (temp.contains("values")) {
                    // values括号里面的内容
                    String values = temp.substring(temp.indexOf("(") + 1, temp.lastIndexOf(")"));
                    // to_date('09-10-2015 15:16:32', 'dd-mm-yyyy hh24:mi:ss')
                    // to_date('10-12-2015', 'dd-mm-yyyy')
                    List<String> stringList = Arrays.asList(values.split(", "));
                    for (int i = 0; i < stringList.size(); i++) {
                        String value = stringList.get(i);
                        if (value.contains("to_date")) {
                            // 格式化双引号之间的日期
                            stringList.set(i, "'" + formatDate(value.substring(value.indexOf("'") + 1, value.lastIndexOf("'"))) + "'");
                        }
                    }
                    // System.out.println("values (" + StringUtils.join(stringList.toArray(), ", ") + ");");
                    temp = "values (" + StringUtils.join(stringList.toArray(), ", ") + ");";
                }
                // 写入一行
                bw.write(temp + "\n");
            }
            // 关闭读取流
            reader.close();
            // 关闭写入流
            bw.close();
            writer.close();
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
        System.out.println("以行为单位读取文件内容，一次读一整行，end...");
    }


    public final static String DATE_PATTERN_0 = "yyyy-MM-dd";
    public final static String DATE_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
    public final static String TIME_PATTERN_0 = "dd-MM-yyyy";
    public final static String TIME_PATTERN_1 = "dd-mm-yyyy HH:mm:ss";

    public static String formatDate(String date) {
        String outDate = "";
        if (StringUtils.isNotEmpty(date)) {
            try {
                String TIME_PATTERN = date.contains(":") ? TIME_PATTERN_1 : TIME_PATTERN_0;
                String DATE_PATTERN = date.contains(":") ? DATE_PATTERN_1 : DATE_PATTERN_0;
                Date dateTime = new SimpleDateFormat(TIME_PATTERN).parse(date);
                outDate = new SimpleDateFormat(DATE_PATTERN).format(dateTime);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        // System.err.println(outDate);
        return outDate;
    }

}
