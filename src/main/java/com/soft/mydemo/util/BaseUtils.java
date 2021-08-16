package com.soft.mydemo.util;

public class BaseUtils {

    /**
     * 根据得到的数据按照逗号转换成数组获得最大值
     *
     * @param data
     * @return
     */
    public static int maxVal(String data) {
        int[] ints = stringToArray(data);
        int result = 0;
        for (int i = 0; i < ints.length; i++) {
            if (i == 0) {
                result = ints[i];
            } else {
                if (ints[i] > result) {
                    result = ints[i];
                }
            }
        }
        return result;
    }

    /**
     * 根据得到的数据按照逗号转换成数组获得最小值
     */
    public static int minVal(String data) {
        int[] ints = stringToArray(data);
        int result = 0;
        for (int i = 0; i < ints.length; i++) {
            if (i == 0) {
                result = ints[i];
            } else {
                if (ints[i] < result) {
                    result = ints[i];
                }
            }
        }
        return result;
    }

    /**
     * String转换成数组
     * 1,2,3,4->[1,2,3,4]
     *
     * @param data
     * @return
     */
    public static int[] stringToArray(String data) {
        String[] arrs = data.split(",");
        int[] result = new int[arrs.length];

        for (int i = 0; i < arrs.length; i++) {
            result[i] = Integer.parseInt(arrs[i]);
        }

        return result;
    }

}
