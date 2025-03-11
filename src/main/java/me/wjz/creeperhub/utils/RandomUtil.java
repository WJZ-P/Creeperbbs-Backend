package me.wjz.creeperhub.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
@Component
public class RandomUtil {
    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    public static int getRandomNumber(int range) {
        return  random.nextInt(range);
    }

}
