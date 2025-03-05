package me.wjz.creeperhub.utils;

import java.util.Random;

public class RandomUtil {
    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+-_";

    public static String getRandomString(int length) {

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(65);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
