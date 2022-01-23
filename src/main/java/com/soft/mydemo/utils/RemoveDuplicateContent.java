package com.soft.mydemo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 去除重复内容
 * @author admin
 */
public class RemoveDuplicateContent {

    public static void main(String[] args) {
        String hostName = "";

        // 先塞到list里面
        String[] list = hostName.trim().split(",");
        List<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, list);
        System.err.println(arrayList);
        System.err.println(arrayList.size());

        // 去重
        List<String> listWithoutDuplicates = arrayList.stream()
                .distinct()
                .collect(Collectors.toList());
        System.err.println(listWithoutDuplicates);
        System.err.println(listWithoutDuplicates.size());
    }
}
