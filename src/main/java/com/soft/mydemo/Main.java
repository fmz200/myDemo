package com.soft.mydemo;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    public static void main00(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            int a = in.nextInt();
            int b = in.nextInt();
            System.out.println(a + b);
        }
    }

    public static void main01(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            List<String> strings = Arrays.asList(str.split(" "));
            System.out.println(strings.get(strings.size() - 1).length());
        }
    }

    // 写出一个程序，接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String a = in.nextLine();
            String b = in.nextLine();
            String str = a.trim().toLowerCase();
            char[] chars = str.toCharArray();
            int count = 0;
            for (char aChar : chars) {
                if (String.valueOf(aChar).equals(b.toLowerCase())) {
                    count++;
                }
            }
            System.out.println(count);
        }
    }
}
