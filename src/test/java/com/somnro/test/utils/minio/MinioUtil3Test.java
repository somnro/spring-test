package com.somnro.test.utils.minio;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author 2021-03-04 14:08
 **/
class MinioUtil3Test {

    /**
     * 检查存储桶是否存在
     */
    @Test
    void bucketExists() {
        System.out.println(MinioUtil3.bucketExists("123"));//true
        System.out.println(MinioUtil3.bucketExists("456"));//false
    }

    /**
     * 创建存储桶
     */
    @Test
    void makeBucket() {
        System.out.println(MinioUtil3.makeBucket("457"));//true
        System.out.println(MinioUtil3.makeBucket("123"));//false
    }

    /**
     * 列出所有存储桶
     */
    @Test
    void listBuckets() {
        System.out.println(MinioUtil3.listBuckets());
    }

    /**
     * 列出所有存储桶名称
     */
    @Test
    void listBucketNames() {
        System.out.println(MinioUtil3.listBucketNames());//[123, 457, 458]
    }

    /**
     * 删除存储桶
     */
    @Test
    void removeBucket() {
        System.out.println(MinioUtil3.removeBucket("458"));//false
        System.out.println(MinioUtil3.removeBucket("457"));//true
    }

    /**
     * 删除存储桶
     */
    @Test
    void listObjectNames() {
        for (String listBucketName : MinioUtil3.listObjectNames("458")) {
            System.out.println(listBucketName);
            /**
             * %E6%8A%A5%E8%AD%A6%E7%BB%9F%E8%AE%A1_1614407738619.xlsx
             * a12/JxcFileController.java
             */
        }
        for (String listBucketName : MinioUtil3.listObjectNames("457")) {
            System.out.println(listBucketName);//NullPointerException
        }
        for (String listBucketName : MinioUtil3.listObjectNames("a11")) {
            System.out.println(listBucketName);//无输出
        }
    }


    /**
     * 通过本地文件上传
     */
    @Test
    void putObject() {
//        System.out.println(MinioUtil3.putObject("458","app.jar","F:\\test\\app.jar"));//true 文件120M上传46s，
        System.out.println(MinioUtil3.putObject("458","0127PlayerControl.7z","F:\\test\\0127PlayerControl.7z"));//true
        System.out.println(MinioUtil3.putObject("458","0127PlayerControl.7z","F:\\test\\0127PlayerControl.7z"));//true
    }

    /**
     * 通过本地文件流方式上传
     */
    @Test
    void putObject2() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("F:\\test\\0127PlayerControl.7z");
        System.out.println(MinioUtil3.putObject("458","0127PlayerControl2222.7z",fileInputStream));//true
    }

    /**
     * 以流的形式获取一个文件对象
     */
    @Test
    void getObject() {
        InputStream inputStream = MinioUtil3.getObject("458", "0127PlayerControl2222.7z");
        FileUtil.writeFromStream(inputStream,"F:\\test\\0127.rar");//检查下载已完成
    }

    /**
     * 以流的形式获取一个文件对象（断点下载）
     */
    @Test
    void getObject2() {
    }

    /**
     * 下载并将文件保存到本地
     */
    @Test
    void getObject3() {
        System.out.println(MinioUtil3.getObject("458", "0127PlayerControl2222.7z","F:\\test\\012701.rar"));//true 检查下载已完成
    }

    /**
     * 删除一个对象
     */
    @Test
    void removeObject() {
        System.out.println(MinioUtil3.removeObject("458","0127PlayerControl2222.7z"));//true
        System.out.println(MinioUtil3.removeObject("458","123.7z"));//true 无对象是也是删除成功
    }

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     */
    @Test
    void removeObject2() {
        for (String s : MinioUtil3.removeObject("458", Arrays.asList("121463","465546.jar"))) {
            System.out.println(s);//无输出,成功删除和失败删除 都无输出。。
        }
        for (String s : MinioUtil3.removeObject("458", Arrays.asList("app.jar","465546"))) {
            System.out.println(s);//无输出
        }
    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     */
    @Test
    void presignedGetObject() {
        System.out.println(MinioUtil3.presignedGetObject("458","0127PlayerControl.7z",120));// 120 秒后失效
        //http://192.168.1.233:9000/458/0127PlayerControl.7z?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin123%2F20210304%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20210304T075519Z&X-Amz-Expires=120&X-Amz-SignedHeaders=host&X-Amz-Signature=e4a0c6fe59978dab523fe1097f7059fb8d4e58efaf5ed23d35560e06e27df41d
    }

    /**
     * 获取对象的元数据
     */
    @Test
    void statObject() {
        System.out.println(MinioUtil3.statObject("458","0127PlayerControl.7z").toString());
        //ObjectStat{bucket='458', name='0127PlayerControl.7z', contentType='application/octet-stream', createdTime=2021-03-04T07:11:13Z, length=2382977, etag='12f2888975861cc9bb053d1d8ae4e9b1-1'}
    }

    /**
     * 获取对象的元数据
     */
    @Test
    void getObjectUrl() {
        System.out.println(MinioUtil3.getObjectUrl("458","0127PlayerControl.7z"));//http://192.168.1.233:9000/458/0127PlayerControl.7z
    }

    /**
     * 文件下载
     */
    @Test
    void down1719() {
        // http://localhost:8888/minio/download/1  浏览器下载成功
        // http://localhost:8888/minio/download/2   浏览器下载成功
    }

    /**
     * 文件上传
     */
    @Test
    void upload1732() {
        // http://localhost:8888/minio/upload/1  使用postman上传成功
    }

}
