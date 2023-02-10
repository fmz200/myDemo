package com.soft.mydemo.demo;

import java.io.File;
import java.util.List;

/**
 * 读取目录下的每个文件和文件夹，并读取文件的内容写到目标文件中去
 */
public class FileCombine {
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

}
