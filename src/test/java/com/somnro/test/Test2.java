package com.somnro.test;

import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.somnro.test.utils.FastDFSUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @author 2021-03-05 16:08
 **/
@SpringBootTest
public class Test2 {
    @Autowired
    private FastDFSUtil fastDFSUtil;

    /**
     * result = group1/M00/00/00/wKgB6WBB6XiAKqtqACRcgR6a1z04806.7z
     * result = group1/M00/00/00/wKgB6WBB6XiAeR1sAAR0yL8Q8SY170.jpg
     */
    @Test
    public void test1608(){
//        fastDFSUtil.upload(new File("F:\\test\\0127PlayerControl.7z"));
        fastDFSUtil.upload(new File("C:\\Users\\Administrator\\Desktop\\123.jpg"));
    }

    /**
     * 测试查询文件
     */
    @Test
    public void test1609(){
        System.out.println(fastDFSUtil.query("group1","M00/00/00/wKgB6WBB6XiAeR1sAAR0yL8Q8SY170.jpg"));
        //source_ip_addr = 192.168.1.233, file_size = 292040, create_timestamp = 1970-01-20 00:35:32, crc32 = -1089408730
    }

    @Test
    public void test1610(){
        System.out.println( fastDFSUtil.delete("group1/M00/00/00/wKgB6WBB6XiAeR1sAAR0yL8Q8SY170.jpg1"));
    }
}
