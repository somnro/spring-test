package com.somnro.test.utils;

import cn.hutool.core.io.FileUtil;
import com.luhuiguo.fastdfs.domain.FileInfo;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author 2021-03-05 14:45
 * FastDFS工具类
 **/
@Component
public class FastDFSUtil {

    @Value("${fdfs.url}")
    private String url;
    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 浏览器文件上传
     */
    public String upload(MultipartFile file){
        String result = null;
        try {
            StorePath storePath = storageClient.uploadFile("group1", file.getInputStream(), file.getSize(), FileUtil.extName(file.getOriginalFilename()));
            result = storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("result = " + result);
        return result;
    }

    /**
     * 本地文件上传
     */
    public String upload(File file){
        String result = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            StorePath storePath = storageClient.uploadFile("group1", fileInputStream,file.length(), FileUtil.extName(file.getName()));
            result = storePath.getFullPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result = " + result);
        return result;
    }

    /**
     * 删除文件
     */
    public Boolean delete(String s){
        try {
            storageClient.deleteFile(s);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询文件
     */
    public FileInfo query(String s1,String s2){
        FileInfo fileInfo = null;
        try {
            fileInfo = storageClient.queryFileInfo(s1, s2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileInfo;
    }


}
