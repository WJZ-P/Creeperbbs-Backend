package me.wjz.creeperhub.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //密码转字节数组并哈希
            byte[] hash = md.digest(password.getBytes());
            //将哈希后的字节数组转成16进制字符串
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            //这里代码解释：
            //如果用Integer.toHexString来计算，接受的参数是一个int，32位，然后是四个字节。
            //但b是byte，是单个字节。用0xff & b是为了只取第八位，也就是b的位置来去做转换。忽略掉高24位。
            //0xff是16进制的255，二进制是11111111，所以0xff & b，就是取b的低八位。
            //那么b本身就只有8位，为什么不直接拿它作为参数？
            //因为toHexString的参数是int，b会被转换成int，而b在转换时会以有符号转换，如果开头是1，
            //那么转换成int后高24位全为1，此时再toHexString就不符合预期了。
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
