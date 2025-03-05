package me.wjz.creeperhub.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {
    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+-_=";
    private static final SecureRandom random = new SecureRandom();
    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(66);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
