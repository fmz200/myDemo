package com.soft.mydemo.util;

/**
 * 路径工具
 *
 * @author magic chen
 * @date 2020/8/20 15:07
 **/
public class PathUtils {
    /**
     * sonarlint提示 add a private constructor to hide the implicit public one
     * 意思是util类里面都是静态方法，不会去初始化这个类，所以不应该暴露一个public构造函数
     * 解决方案：定义一个private构造函数
     */
    private PathUtils() {
        throw new IllegalStateException("PathUtils class");
    }

    /**
     * 获得当前运行环境path
     *
     * @author magic chen
     * @date 2020/8/20 15:09
     **/
    public static String getPath() {
        String path = PathUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1);
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            path = path.substring(0, path.lastIndexOf("/") + 1);
        }
        path = path.replace("target/classes/", "").replace("file:", "");
        return path;
    }

}
