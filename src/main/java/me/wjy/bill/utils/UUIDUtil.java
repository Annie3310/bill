package me.wjy.bill.utils;

import java.util.UUID;

/**
 * @author ηιδΉ
 * @date 2021/8/3
 */
public class UUIDUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getUUID(int len) {
        if (0 >= len) {
            return null;
        }
        String uuid = getUUID();
        StringBuilder stringBuilder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            stringBuilder.append(uuid.charAt(i));
        }
        return stringBuilder.toString();
    }
}
