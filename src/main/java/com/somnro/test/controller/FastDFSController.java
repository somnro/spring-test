package com.somnro.test.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.somnro.test.utils.FastDFSUtil;
import com.somnro.test.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 2021-03-05 11:23
 **/
@RestController
@RequestMapping("/fs")
public class FastDFSController {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private FastDFSUtil fastDFSUtil;

    /**
     *  192.168.1.233:8888/group1/M00/00/00/wKgB6WBBxh2AUU8yAAR0yL8Q8SY22.file
     * 返回： group1/M00/00/00/wKgB6WBBxh2AUU8yAAR0yL8Q8SY22.file
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        return fastDFSUtil.upload(file);
    }

    /**
     * 打印 MultipartFile 的参数值
     * getName:file
     * getBytes:[B@358e7586
     * isEmpty:false
     * getSize:292040
     * getInputStream:java.io.FileInputStream@3545208a
     * getContentType:image/jpeg
     * getOriginalFilename:123.jpg
     *
     */
    @RequestMapping("/upload1")
    @ResponseBody
    public String upload1(@RequestParam("file") MultipartFile file) throws Exception {
        Utils.printMethod(file);
        return "";
    }
}
