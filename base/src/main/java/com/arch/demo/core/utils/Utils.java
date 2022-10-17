/*
 * Copyright (C) 2010-2013 TENCENT Inc.All Rights Reserved.
 *
 * Description:  与应用和业务无关的工具类
 *
 * History:
 *  1.0   dancycai (dancycai@tencent.com) 2013-7-10   Created
 */

package com.arch.demo.core.utils;

import android.util.SparseArray;

import java.util.Collection;
import java.util.Map;

public class Utils {
    private static final String TAG = "Utils";
    private static final boolean DEBUG = false;

    public static boolean isEmpty(final String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean isEmpty(final SparseArray sparseArray) {
        return sparseArray == null || sparseArray.size() <= 0;
    }

    public static boolean isEmpty(final Collection<? extends Object> collection) {
        return collection == null || collection.size() <= 0;
    }

    public static boolean isEmpty(final Map<? extends Object, ? extends Object> list) {
        return list == null || list.size() <= 0;
    }
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }

        if (object instanceof Collection) {
            return isEmpty((Collection<?>) object);
        } else if (object instanceof Map) {
            return isEmpty((Map<?, ?>) object);
        }

        String str = object.toString();
        return isEmpty(str);
    }
    /**判断字符串是否是手机号***/
    public static boolean isPhone(String phone){
        String telRegex = "[1][3456789]\\d{9}";
        // "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (isEmpty(phone)) {
            return false;
        } else {
            return phone.matches(telRegex);
        }
    }

}
