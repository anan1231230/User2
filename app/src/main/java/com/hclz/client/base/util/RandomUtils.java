package com.hclz.client.base.util;

import java.util.Map;
import java.util.Random;

public class RandomUtils {
    private static Random random;

    // 双重校验锁获取一个Random单例
    public static Random getRandom() {
        if (random == null) {
            synchronized (RandomUtils.class) {
                if (random == null) {
                    random = new Random();
                }
            }
        }
        return random;
    }

    /**
     * 获得一个[0,max)之间的整数。
     *
     * @param max
     * @return
     */
    public static int getRandomInt(int max) {
        return Math.abs(getRandom().nextInt()) % max;
    }

    /**
     * 从map中随机取得一个key
     *
     * @param map
     * @return
     */
    public static String getRandomKeyFromMap(Map<String, String> map) {
        int rn = getRandomInt(map.size());
        int i = 0;
        for (String key : map.keySet()) {
            if (i == rn) {
                return key;
            }
            i++;
        }
        return null;
    }

}
