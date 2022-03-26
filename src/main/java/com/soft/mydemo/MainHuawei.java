package com.soft.mydemo;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class MainHuawei {
    public static void main01(String[] args) {
        Scanner in = new Scanner(System.in);
        /*
         * 1、标点符号问题(正则匹配)
         * 2、缩写存在单引号问题ok
         * 3、字典排序问题ok
         */
        /*
         * The furthest distance in the "world"! Is not between life and death, But when I stand in front of you, Yet you don't know that I love you.
         * f
         */
        while (in.hasNextLine()) {
            String str = in.nextLine();
            String pre = in.nextLine();
            String regex = "[a-zA-Z]";
            // 处理缩写单引号
            String[] split01 = str.split("'");
            // 处理空格拿到单词(去重)
            TreeSet<String> split02 = new TreeSet<>();
            for (String split : split01) {
                String[] strings = split.split(" "); // 每个单词
                for (String string : strings) {
                    char[] chars = string.toCharArray();
                    StringBuilder result = new StringBuilder();
                    for (char aChar : chars) {
                        if (String.valueOf(aChar).matches(regex)) {
                            result.append(aChar);
                        }
                    }
                    split02.add(result.toString());
                }
            }
            // 匹配单词
            StringBuilder result = new StringBuilder();
            for (String word : split02) {
                if (word.toLowerCase().startsWith(pre)) {
                    result.append(word).append(" ");
                }
            }
            if (result.length() == 0) {
                System.out.println(pre);
            } else {
                System.out.println(result.toString().trim());
            }
        }
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 21,30,62,5,31,30,1,6,3,13,45,28
        // 5,21
        while (in.hasNextLine()) {
            String str = in.nextLine();
            List<String> strings = Arrays.asList(str.split(","));
            TreeSet<Integer> split = new TreeSet<>();
            for (String word : strings) {
                split.add(Integer.parseInt(word));
            }
            StringBuilder result = new StringBuilder();
            /*
             * 需要比较有几位数，不能直接取前三个
             * 1、按照数字位数分组并按位数大小排序
             * 2、取第一组，若不够3个则接着取第二组，如不够取第三组
             * 3、若够则比较第二组每位数截取的第一组的大小与第一组数字比较，再进行排序组合
             */
            if (strings.size() == 1) {
                result.append(strings.get(0));
            } else if (strings.size() == 2) {
                // 0,5  1,1   2,6   3,21  23,2232
                String s1 = strings.get(0);
                String s2 = strings.get(1);
                int l1 = s1.length();
                int l2 = s2.length();

                if (l1 > l2) { // 3732,23
                    int sb1 = Integer.parseInt(s1.substring(0, l2));
                    if (sb1 >= Integer.parseInt(s1)) {
                        result.append(s1).append(s2);
                    } else {
                        result.append(s2).append(s1);
                    }
                } else if (l1 < l2) { // 23,2232
                    int sb2 = Integer.parseInt(s2.substring(0, l1));
                    if (sb2 >= Integer.parseInt(s1)) {
                        result.append(s1).append(s2);
                    } else {
                        result.append(s2).append(s1);
                    }
                } else {
                    if (Integer.parseInt(s1) >= Integer.parseInt(s2)) {
                        result.append(s2).append(s1);
                    } else {
                        result.append(s1).append(s2);
                    }
                }
            } else if (strings.size() >= 3) {
                // TODO
                Arrays.sort(strings.toArray());
                int count = 0;
                for (Integer i : split) {
                    if (count == 3) {
                        break;
                    }
                    result.append(i);
                    count++;
                }
            }
            System.out.println(result);
        }
    }
}
