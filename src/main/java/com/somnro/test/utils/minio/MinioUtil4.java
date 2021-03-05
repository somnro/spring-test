package com.somnro.test.utils.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 2021-03-05 9:26
 * 自动注入bean，由spring管理
 **/
@Component
public class MinioUtil4 {
    @Autowired
    private MinioClient minioClient;

    /**
     * 检查存储桶是否存在
     * @param bucketName 存储桶名称
     */
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(bucketName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建存储桶
     * @param bucketName 存储桶名称
     */
    public boolean makeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            try {
                minioClient.makeBucket(bucketName);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
