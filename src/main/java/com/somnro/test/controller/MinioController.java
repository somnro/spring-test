package com.somnro.test.controller;

import com.somnro.test.config.MinioConfig;
import com.somnro.test.utils.minio.MinioUtil3;
import com.somnro.test.utils.minio.MinioUtil4;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 2021-03-04 17:05
 **/
@RestController
@RequestMapping("/minio")
public class MinioController {

    /**
     * 使用配置MinioConfig注入bean
     */
    @Autowired
    private MinioUtil4 minioUtil4;

    @GetMapping("/download/1")
    public void download(HttpServletResponse response){
        MinioUtil3.downloadFile("458","0127PlayerControl.7z","12311.jpg",response);
    }

    @GetMapping("/download/2")
    public void download2(HttpServletResponse response){
        MinioUtil3.downloadFile("458","123.png","12311.jpg",response);
    }

    @GetMapping("/upload/1")
    public void upload(@RequestParam("file") MultipartFile file){
        MinioUtil3.upload("458",file,"12345666.png");
    }

    /**
     * 使用配置MinioConfig注入bean,同一个对象
     * 请求地址：http://localhost:8888/minio/makeBucket1
     */
    @GetMapping("/makeBucket1")
    @ResponseBody
    public String makeBucket1(){
        return minioUtil4.makeBucket("930")+"";
    }

    /**
     * http://localhost:8888/minio/makeBucket/945
     */
    @GetMapping("/makeBucket/{id}")
    @ResponseBody
    public String makeBucket1(@PathVariable String id){
        return minioUtil4.makeBucket(id)+"";
    }

}
