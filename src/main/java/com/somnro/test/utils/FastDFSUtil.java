package com.somnro.test.utils;

import cn.hutool.core.io.FileUtil;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 2021-03-05 14:45
 **/
@Component
public class FastDFSUtil {
    @Autowired
    private FastFileStorageClient storageClient;

    public String upload(MultipartFile file){
        String result = null;
        try {
            StorePath storePath = storageClient.uploadFile("group1", file.getInputStream(), file.getSize(), FileUtil.extName(file.getOriginalFilename()));
            result = storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
