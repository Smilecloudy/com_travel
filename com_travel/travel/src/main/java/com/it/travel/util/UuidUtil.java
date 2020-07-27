package com.it.travel.util;

import java.util.UUID;

/**
 * @auther: cyb
 * @create: 2020/7/26 15:05
 */

/**
 * 产生UUID随机字符串工具类
 */
public final class UuidUtil {
    private UuidUtil() {
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        System.out.println(UuidUtil.getUuid());
        System.out.println(UuidUtil.getUuid());
        System.out.println(UuidUtil.getUuid());
        System.out.println(UuidUtil.getUuid());
    }
}


