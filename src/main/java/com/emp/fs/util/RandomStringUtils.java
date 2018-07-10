package com.emp.fs.util;

import java.util.Random;

public class RandomStringUtils {
    private static final String COD = "12356789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ12356789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";

    public static String getPassword() {
        char str[] = COD.toCharArray();
        StringBuilder pwd = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            Random random = new Random();
            int num = random.nextInt(100);
            pwd.append(str[num]);
        }
        return pwd.toString();
    }
}
