package com.somnro.test;

import cn.hutool.core.util.ClassUtil;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;

/**
 * @author 2021-03-05 13:29
 **/
public class Test1 {

    /**
     * 打印方法上有几个参数
     */
    @Test
    public void test1330(){
        Method[] declaredMethods = ClassUtil.getDeclaredMethods(MultipartFile.class);
        for (Method declaredMethod : declaredMethods) {
            /**
             * getName0
             * getBytes0
             * isEmpty0
             * getResource0
             * getSize0
             * getInputStream0
             * getContentType0
             * transferTo1
             * transferTo1
             * getOriginalFilename0
             */
            System.out.println(declaredMethod.getName()+declaredMethod.getParameterCount());

        }
    }
}
