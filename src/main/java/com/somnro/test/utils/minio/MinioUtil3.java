package com.somnro.test.utils.minio;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * minio 单独工具类 基于7.0.2版本
 * SneakyThrows 依赖lombok
 **/

public class MinioUtil3 {

    private static String minioUrl = "http://192.168.1.233:9000/";
    private static String minioName = "minioadmin123";
    private static String minioPass = "minioadmin123";
    private static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;
    private static MinioClient minioClient = getMinioClient();

    public static MinioClient getMinioClient() {
        try {
            if (minioClient == null) {
                return new MinioClient(minioUrl, minioName, minioPass);
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        return minioClient;
    }

    /**
     * 检查存储桶是否存在
     * @param bucketName 存储桶名称
     */
    public static boolean bucketExists(String bucketName) {
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
    public static boolean makeBucket(String bucketName) {
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

    /**
     * 列出所有存储桶名称
     */
    public static List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 列出所有存储桶
     */
    public static List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除存储桶
     * @param bucketName 存储桶名称
     */
    public static boolean removeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                Iterable<Result<Item>> myObjects = listObjects(bucketName);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    // 有对象文件，则删除失败
                    if (item.size() > 0) {
                        return false;
                    }
                }
                // 删除存储桶，注意，只有存储桶为空时才能删除成功。
                minioClient.removeBucket(bucketName);
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象名称
     * @param bucketName 存储桶名称
     */
    public static List<String> listObjectNames(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                List<String> listObjectNames = new ArrayList<>();
                Iterable<Result<Item>> myObjects = listObjects(bucketName);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    listObjectNames.add(item.objectName());
                }
                return listObjectNames;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 列出存储桶中的所有对象
     * @param bucketName 存储桶名称
     */
    public static Iterable<Result<Item>> listObjects(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                return minioClient.listObjects(bucketName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过文件上传
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return
     */
    public static boolean putObject(String bucketName, String objectName, String fileName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                minioClient.putObject(bucketName, objectName, fileName, null);
                ObjectStat statObject = statObject(bucketName, objectName);
                if (statObject != null && statObject.length() > 0) {
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 通过InputStream上传对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param stream     要上传的流
     * @return
     */
    public static boolean putObject(String bucketName, String objectName, InputStream stream) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                minioClient.putObject(bucketName, objectName, stream, new PutObjectOptions(stream.available(), -1));
                ObjectStat statObject = statObject(bucketName, objectName);
                if (statObject != null && statObject.length() > 0) {
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    public static InputStream getObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                ObjectStat statObject = statObject(bucketName, objectName);
                if (statObject != null && statObject.length() > 0) {
                    InputStream stream = minioClient.getObject(bucketName, objectName);
                    return stream;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 以流的形式获取一个文件对象（断点下载）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return
     */
    public InputStream getObject(String bucketName, String objectName, long offset, Long length) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                ObjectStat statObject = statObject(bucketName, objectName);
                if (statObject != null && statObject.length() > 0) {
                    InputStream stream = minioClient.getObject(bucketName, objectName, offset, length);
                    return stream;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 下载并将文件保存到本地
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return
     */
    public static boolean getObject(String bucketName, String objectName, String fileName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                ObjectStat statObject = statObject(bucketName, objectName);
                if (statObject != null && statObject.length() > 0) {
                    minioClient.getObject(bucketName, objectName, fileName);
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    public static boolean removeObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                minioClient.removeObject(bucketName, objectName);
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     *
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象
     * @return
     */
    public static List<String> removeObject(String bucketName, List<String> objectNames) {
        List<String> deleteErrorNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                Iterable<Result<DeleteError>> results = minioClient.removeObjects(bucketName, objectNames);
                for (Result<DeleteError> result : results) {
                    DeleteError error = result.get();
                    deleteErrorNames.add(error.objectName());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return deleteErrorNames;
    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    public static String presignedGetObject(String bucketName, String objectName, Integer expires) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            try {
                if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                    throw new InvalidExpiresRangeException(expires,
                            "expires must be in range of 1 to " + DEFAULT_EXPIRY_TIME);
                }
                url = minioClient.presignedGetObject(bucketName, objectName, expires);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return url;
    }

    /**
     * 生成一个给HTTP PUT请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行上传，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    public String presignedPutObject(String bucketName, String objectName, Integer expires) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            try {
                if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                    throw new InvalidExpiresRangeException(expires,
                            "expires must be in range of 1 to " + DEFAULT_EXPIRY_TIME);
                }
                url = minioClient.presignedPutObject(bucketName, objectName, expires);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return url;
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    public static ObjectStat statObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                ObjectStat statObject = minioClient.statObject(bucketName, objectName);
                return statObject;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    public static String getObjectUrl(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            try {
                url = minioClient.getObjectUrl(bucketName, objectName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return url;
    }


    /**
     * 此方式适用于springboot 的文件下载
     * HttpServletResponse 依赖于 spring-boot-starter-web
     */
    public static void downloadFile(String bucketName, String fileName, String originalName, HttpServletResponse response) {
        try {

            InputStream file = minioClient.getObject(bucketName, fileName);
            String filename = new String(fileName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            if (!"".equals(originalName)) {
                fileName = originalName;
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 此方式适用于springboot 的文件上传
     *
     * @param bucketName
     * @param multipartFile
     */
    public static void upload(String bucketName, MultipartFile multipartFile, String filename) {
        PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
        putObjectOptions.setContentType(multipartFile.getContentType());
        try {
            minioClient.putObject(bucketName, filename, multipartFile.getInputStream(), putObjectOptions);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
