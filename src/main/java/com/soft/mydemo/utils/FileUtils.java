package com.soft.mydemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.soft.mydemo.common.CommonConstants;
import com.soft.mydemo.util.HttpClientUtil;
import com.soft.mydemo.util.HttpRequest;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件处理
 *
 * @author admin
 * @date 2021/08/05
 */
public class FileUtils {

    private static final List<String> nodes = new ArrayList<>();
    private static int index = 1;

    public static void main0(String[] args) {
        String s = "";
        StringBuffer s1 = new StringBuffer(s);
        int index;
        for (index = CommonConstants.FEED_LENGTH; index < s1.length(); index += 6) {
            s1.insert(index, '\n');
        }
        System.out.println(s + "每隔76个字符换行：");
        System.err.println(s1);
    }

    public static void main1(String[] args) {
        File f0 = new File("C:\\Users\\admin\\Desktop\\01.txt");
        if (!f0.exists()) {
            return;
        }
        File f1 = new File("C:\\Users\\admin\\Desktop\\01out.txt");
        try {
            FileReader fr = new FileReader(f0);
            FileWriter fw = new FileWriter(f1);
            int ch;
            int size = 0;
            while ((ch = fr.read()) != -1) {
                fw.write(ch);
                // 每隔76个字符换行
                if (++size % CommonConstants.FEED_LENGTH == 0) {
                    fw.write('\n');
                }
            }
            fr.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取源文件，每隔一定长度后换行并输出到新文件
     */
    @Test
    public void main2() throws IOException {
        File srcFile = new File("d:\\test.txt");
        File outFile = new File("d:\\dest.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(srcFile));
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile));
        char[] buffer = new char[CommonConstants.FEED_LENGTH];
        while (reader.read(buffer) != -1) {
            System.out.println(buffer);
            writer.write(String.valueOf(buffer).trim() + "\r\n");
        }
        reader.close();
        writer.flush();
        writer.close();
    }

    public static void main04(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入源目录：");
        String sourcePath = sc.nextLine();
        System.out.println("请输入新目录：");
        String path = sc.nextLine();

        copyDir(sourcePath, path);
    }

    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath); //文件名称列表
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            boolean mkdir = (new File(newPath)).mkdir();
            System.err.println("mkdir = " + mkdir);
        }

        assert filePath != null;
        for (String s : filePath) {
            if ((new File(oldPath + File.separator + s)).isDirectory()) {
                copyDir(oldPath + File.separator + s, newPath + File.separator + s);
            }

            if (new File(oldPath + File.separator + s).isFile()) {
                copyFile(oldPath + File.separator + s, newPath + File.separator + s);
            }
        }
    }

    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);

        byte[] buffer = new byte[2097152];

        while ((in.read(buffer)) != -1) {
            out.write(buffer);
        }
    }

    /**
     * 根据地址获得数据的输入流
     *
     * @param strUrl 网络连接地址
     * @return url的输入流
     */
    public static InputStream getInputStreamByUrl(String strUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过url读取文件并逐行打印处理
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> remotePaths = new ArrayList<>();
        remotePaths.add("https://raw.githubusercontent.com/hookzof/socks5_list/master/proxy.txt");
        remotePaths.add("https://raw.githubusercontent.com/TheSpeedX/PROXY-List/master/http.txt");
        remotePaths.add("https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/socks5.txt");
        remotePaths.add("https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/socks4.txt");
        BufferedReader bufferedReader = null;
        for (String remotePath : remotePaths) {
            InputStream inputStreamByUrl = getInputStreamByUrl(remotePath);
            // BufferedReader是可以按行读取文件
            if (inputStreamByUrl != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStreamByUrl));
                String str;
                // socks5=example.com:80, fast-open=false, udp-relay=false, tag=socks5-01
                // http=http.example.com:80, fast-open=false, udp-relay=false, tag=http
                String prefix = "";
                String suffix = ", fast-open=false, udp-relay=false, tag=";
                if (remotePath.contains("socks5")) {
                    prefix = "socks5=";
                } else if (remotePath.contains("socks4")) {
                    prefix = "socks4=";
                } else if (remotePath.contains("http")) {
                    prefix = "http=";
                }
                List<String> ipList = new ArrayList<>();
                Map<String, String> ipAndPortList = new HashMap<>();
                // 遍历这个文件的所有行
                while ((str = bufferedReader.readLine()) != null) {
                    ipList.add(str.split(":")[0]);
                    ipAndPortList.put(str.split(":")[0], str);
                }
                queryIpAttrArray(ipList, ipAndPortList, prefix, suffix);

            }

        }
        System.out.println("遍历结束, 开始打印...");
        nodes.forEach(System.out::println);
        //close
        bufferedReader.close();
    }

    public static void queryIpAttrArray(List<String> ipList, Map<String, String> ipAndPortList, String prefix, String suffix) {
        String url = "http://ip-api.com/batch?lang=zh-CN";
        // 每100个元素分割为一个list
        List<List<String>> partition = Lists.partition(ipList, 100);
        AtomicInteger read = new AtomicInteger(1);
        partition.forEach(list -> {
            if (read.get() % 5 == 0) {
                // System.err.println("开始睡觉...");
                try {
                    Thread.sleep(30000); // 每发送 个请求睡一段时间
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // System.err.println("结束睡觉...");
            }

            String getResult = HttpClientUtil.doPostJson(url, JSON.toJSONString(list));
            List<JSONObject> lists = JSON.parseArray(getResult, JSONObject.class);
            for (JSONObject object : lists) {
                if ("success".equals(object.get("status"))) {
                    String name = object.getString("country").concat("-").concat(object.getString("countryCode"))
                            .concat("-").concat(object.getString("regionName"));
                    String line = prefix + ipAndPortList.get(object.getString("query")) + suffix + name
                            + "-" + prefix.substring(0, prefix.length() - 1) + "-" + index;
                    nodes.add(line);
                    index++; // 总数量+1
                }

            }
            read.getAndIncrement();

        });
    }

    /**
     * 参考文档：https://ip-api.com/docs
     * 经测试批量发送45次就会返回Server returned HTTP response code: 429 发送GET请求出现异常！
     *
     * @param ip ip地址
     * @return ip归属地
     * {
     * "query": "41.217.204.26",
     * "status": "success",
     * "country": "尼日利亚",
     * "countryCode": "NG",
     * "region": "FC",
     * "regionName": "FCT",
     * "city": "阿布贾",
     * "zip": "",
     * "lat": 9.0579,
     * "lon": 7.4951,
     * "timezone": "Africa/Lagos",
     * "isp": "Static Pro-health",
     * "org": "",
     * "as": "AS37125 Layer3 Limited"
     * }
     */
    public static String queryIpAttribution(String ip) {
        String url = String.format("http://ip-api.com/json/%s?lang=zh-CN", ip);
        String getResult = HttpRequest.sendGet(url, "");
        JSONObject object = JSON.parseObject(getResult);
        if ("success".equals(object.get("status"))) {
            return object.getString("country").concat("-").concat(object.getString("countryCode"))
                    .concat("-").concat(object.getString("regionName"));
        }
        return ip;
    }


}
