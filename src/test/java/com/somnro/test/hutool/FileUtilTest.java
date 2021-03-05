package com.somnro.test.hutool;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

/**
 * @author 2021-03-05 11:33
 **/
public class FileUtilTest {

    /**
     * 文件后缀名获取
     */
    @Test
    public void test1133(){
        System.out.println(FileUtil.extName("F:\\test\\0127PlayerControl.7z"));//7z
        System.out.println(FileUtil.getSuffix("F:\\test\\0127PlayerControl.7z"));//7z 底层还是调用了extName方法
    }

    /**
     * 文件前缀名、名称
     */
    @Test
    public void test1134(){
        System.out.println(FileUtil.getName("F:\\test\\0127PlayerControl.7z"));//0127PlayerControl.7z
        System.out.println(FileUtil.getPrefix("F:\\test\\0127PlayerControl.7z"));//0127PlayerControl
        System.out.println(FileUtil.mainName("F:\\test\\0127PlayerControl.7z"));//0127PlayerControl
    }

}
