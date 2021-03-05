package com.somnro.test.utils;

import java.lang.reflect.Method;

/**
 * @author 2021-03-05 14:28
 **/
public class Utils {

    /**
     * 打印该对象的所有无参方法值
     */
    public static void printMethod(Object o) throws Exception {
        Method[] declaredMethods = o.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getParameterCount()==0) {
                Method temp = o.getClass().getDeclaredMethod(declaredMethod.getName(), null);
                temp.setAccessible(true);
                System.out.println(declaredMethod.getName() + ":" + temp.invoke(o, null));
            }
        }
    }
}
