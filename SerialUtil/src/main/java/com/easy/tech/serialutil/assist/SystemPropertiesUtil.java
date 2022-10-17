package com.easy.tech.serialutil.assist;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

public class SystemPropertiesUtil {
    public static final String sUNKNOW = "UNKNOWN";

    public static void setProperty(@NonNull String key, @NonNull String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(@NonNull String key) {
        return getProperty(key, sUNKNOW);
    }

    public static String getProperty(@NonNull String key, String value) {
        String result = sUNKNOW;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            result = (String) get.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
