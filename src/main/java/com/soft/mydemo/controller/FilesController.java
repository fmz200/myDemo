package com.soft.mydemo.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.soft.mydemo.bean.RespBean;
import com.soft.mydemo.bean.filesInfo.FilesInfoBean;
import com.soft.mydemo.bean.filesInfo.InterfaceInfoListBean;
import com.soft.mydemo.mapper.FilesInfoMapper;
import com.soft.mydemo.util.FileUtils;
import com.soft.mydemo.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/filesController")
public class FilesController {

    // 上传路径
    @Value("${upload.blog}")
    private String blog;

    @Autowired
    FilesInfoMapper filesInfoMapper;

    /**
     * 文件查询
     *
     * @param filesInfoParams 请求报文
     * @return 响应
     */
    @RequestMapping(value = "/queryFilesInfo")
    @ResponseBody
    public Map<String, Object> queryFilesInfo(FilesInfoBean filesInfoParams) {
        log.debug("queryFilesInfo begin... filesInfoParams is {}", JSON.toJSONString(filesInfoParams));

        PageMethod.startPage(filesInfoParams.getPageNum(), filesInfoParams.getPageSize());
        List<FilesInfoBean> filesInfoList = filesInfoMapper.queryFilesInfo(filesInfoParams);
        PageInfo<FilesInfoBean> pageInfo = new PageInfo<>(filesInfoList);

        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", pageInfo.getTotal());
        map.put("filesInfoList", filesInfoList);
        log.debug("queryFilesInfo end... map is {}", JSON.toJSONString(map));
        return map;
    }

    @RequestMapping(value = "/uploadFiles")
    public RespBean uploadFiles(HttpServletRequest req, MultipartFile image) {
        StringBuilder url = new StringBuilder();
        String filePath = blog + TimeUtils.getCurrentDateString();
        String imgFolderPath = req.getServletContext().getRealPath(filePath);
        File imgFolder = new File(filePath);
        if (!imgFolder.exists()) {
            boolean mkdirs = imgFolder.mkdirs();
            log.info("mkdirs is {}", mkdirs);
        }

        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        String pathInfo = req.getPathInfo();
        String pathTranslated = req.getPathTranslated();
        log.debug("serverName is {},serverPort is {},contextPath is {} ,{} ,{}", serverName, serverPort, contextPath, pathInfo, pathTranslated);
        url.append(req.getScheme())
                .append("://")
                .append(req.getServerName())
                .append(":")
                .append(req.getServerPort())
                .append(req.getContextPath())
                .append(filePath);
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            org.apache.commons.io.IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            // url.append("/").append(imgName);
            filePath = filePath + "/"+imgName;
            return new RespBean("success", filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RespBean("error", "上传失败!");
    }

    /**
     * 文件下载
     *
     * @param infoBeanList 请求报文
     */
    @RequestMapping(value = "/downloadInterfaceLogs")
    public void downloadFiles(InterfaceInfoListBean infoBeanList, HttpServletResponse response) {
        log.debug("downloadInterfaceLogs start... infoBeanList is {}", infoBeanList);
        if (ObjectUtils.isEmpty(infoBeanList) || (StringUtils.isEmpty(infoBeanList.getParamListString())
                && CollectionUtils.isEmpty(infoBeanList.getInterfaceInfoBeanList()))) {
            log.debug("downloadInterfaceLogs end... infoBeanList is empty");
            return;
            //return JSONResult("未获取到页面参数!", 400);
        }

        try {
            // List<FilesInfoBean> interfaceInfoBeanList = JSON.parseArray(infoBeanList.getParamListString(), FilesInfoBean.class);
            List<String> stringList = infoBeanList.uuidSplit();
            if (CollectionUtils.isEmpty(stringList)) {
                return;
            }
            String zipID = UUID.randomUUID().toString().replace("-", "");
            // 临时文件，下载后自动删除
            String zipFilePath = "D:\\interfaceLogZip" + zipID + ".zip";
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
            BufferedOutputStream bos = new BufferedOutputStream(zipOut);
            for (String uuid : stringList) {
                //下载
                FilesInfoBean logForOne = filesInfoMapper.queryFilesInfoForOne(uuid);
                log.debug("downloadInterfaceLogs.logForOne is {}", logForOne);

                String downloadPath = logForOne.getFilePtah();
                // 下载路径为空
                if (StringUtils.isEmpty(downloadPath)) {
                    continue;
                }
                // downloadPath = "D:\\home\\upload\\rg\\01\\20210622\\1398.docx";
                byte[] buffer = FileUtils.downloadIO(downloadPath);
                if (buffer == null) {
                    log.debug("downloadInterfaceLogs.buffer is empty,uuid is {}", logForOne.getFileId());
                    continue;
                }
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(buffer));
                String fileName = logForOne.getFileId() + "_" + logForOne.getFileName() + downloadPath.substring(downloadPath.lastIndexOf(".") - 1);
                zipOut.putNextEntry(new ZipEntry(fileName));

                int len;
                byte[] buf = new byte[10 * 1024];
                while ((len = bis.read(buf, 0, buf.length)) != -1) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
                bis.close();
            }
            bos.close();
            // 最终下载的文件名
            String downloadFileName = TimeUtils.getCurrTimeString() + "日志文件.zip";
            response.setContentType("application/zip;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Disposition", "attchment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));

            ServletOutputStream outputStream = response.getOutputStream();
            FileInputStream inputStream = new FileInputStream(zipFilePath);
            IOUtils.copy(inputStream, outputStream);
            // 关闭输入流
            inputStream.close();
            outputStream.flush();
            //下载完成之后，删掉这个zip包
            File fileTempZip = new File(zipFilePath);
            boolean delete = fileTempZip.delete();
            // 压缩完成后,关闭压缩流
            // zipOut.hashCode();
        } catch (IOException e) {
            log.error("downloadInterfaceLogs error...", e);
            //return JSONResult("下载失败!" + e, 500);
        }
        log.debug("downloadInterfaceLogs end...");
        //return JSONResult("下载成功!", 200);
    }

    /**
     * 日志查看
     *
     * @param uuid 文件标识
     * @return 文件信息
     * @date 2021-12-08
     */
    @RequestMapping(value = "/viewLog")
    public Map<String, Object> viewLog(String uuid) {
        log.debug("viewLog start... uuid is {}", uuid);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("mag", "success");

        FilesInfoBean logForOne = filesInfoMapper.queryFilesInfoForOne(uuid);
        log.debug("viewLog.logForOne is {}", logForOne);
        String downloadPath = logForOne.getFilePtah();
        // 下载路径为空
        if (StringUtils.isEmpty(downloadPath)) {
            map.put("code", 400);
            map.put("msg", "未查询到文件下载路径");
            return map;
        }
        //downloadPath = "D:\\home\\upload\\rg\\CommissionTrial_PaymentResultWriteBack_6b5378db970c4a84a5c9af2dc90d48abb.log";

        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            //创建文件
            File file = new File(downloadPath);
            //获取文件输入流
            fileInputStream = new FileInputStream(file);
            //使用utf-8读流
            inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            //读行
            String lineString = bufferedReader.readLine();
            //判断，是否数据
            while (lineString != null) {
                // ‘\n’是为了在前端展示时换行
                stringBuilder.append("<p>").append(lineString).append("</p>").append("\n");
                lineString = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "下载失败：" + e);
            return map;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logForOne.setContent(stringBuilder.toString());
        map.put("logInfo", logForOne);
        log.debug("viewLog end...");
        return map;
    }

}
