package me.wjz.creeperhub.utils;

public class RegexUtil {
    public static boolean isPhoneValid(String phone) {
        //去掉空格和短横线
        phone = phone.replaceAll("[-\\s]", "");
        return phone.matches("^(\\+86)?1[3456789]\\d{9}$");
    }
}
