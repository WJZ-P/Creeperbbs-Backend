package me.wjz.creeperhub.utils;

import me.wjz.creeperhub.entity.User;

/**
 * 上下文工具
 */
public class UserContent {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }
    public static User getUser() {
        return userThreadLocal.get();
    }
    public static void remove() {
        userThreadLocal.remove();
    }

}
