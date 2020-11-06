package com.zlyandroid.wanandroid.util;


/**
 * 随机数
 *
 * @author zhangliyang
 * @date 2018/7/9-下午4:56
 */
public class RandomUtils {

    public static String randomLetter(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int r = (int) (Math.random() * 26);
            char c = Math.random() > 0.5 ? 'a' : 'A';
            s.append((char) (c + r));
        }
        return s.toString();
    }
}
