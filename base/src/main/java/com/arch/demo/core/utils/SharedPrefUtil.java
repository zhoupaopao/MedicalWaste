package com.arch.demo.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.arch.demo.core.BaseApplication;
import com.arch.demo.core.model.UserBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * SharedPreferences工具类
 */
public class SharedPrefUtil {
    public static final String Token = "TOKEN";
    public static final String USER_Bean="userbean";
    public static final String EmployeeLabel="employeeLabel";


    private static SharedPreferences sp;

    private static SharedPreferences getSp() {
        if (sp == null) {

            sp = BaseApplication.getAppContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        }
        return sp;
    }
    /**
     * 存入字符串
     *
     * @param key   字符串的键
     * @param value 字符串的值
     */
    public static void putString(String key, String value) {
        SharedPreferences preferences = getSp();
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeString(String key) {
        SharedPreferences preferences = getSp();
        //删除指定数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
    public static void clear() {
        SharedPreferences preferences = getSp();
        //删除全部数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * 获取字符串
     *
     * @param key 字符串的键
     * @return 得到的字符串
     */
    public static String getString(String key) {
        SharedPreferences preferences = getSp();
        return preferences.getString(key, "");
    }
    /**
     * 获取字符串
     *
     * @param key   字符串的键
     * @param value 字符串的默认值
     * @return 得到的字符串
     */
    public static String getString(String key, String value) {
        SharedPreferences preferences = getSp();
        return preferences.getString(key, value);
    }
    /**
     * 保存布尔值
     *
     * @param key   键
     * @param value 值
     */
    public static void putBoolean(String key, boolean value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取布尔值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 返回保存的值
     */
    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = getSp();
        return sp.getBoolean(key, defValue);
    }

    /**
     * 保存long值
     *
     * @param key   键
     * @param value 值
     */
    public static void putLong(String key, long value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取long值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static long getLong(String key, long defValue) {
        SharedPreferences sp = getSp();
        return sp.getLong(key, defValue);
    }

    /**
     * 保存int值
     *
     * @param key   键
     * @param value 值
     */
    public static void putInt(String key, int value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取long值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static int getInt(String key, int defValue) {
        SharedPreferences sp = getSp();
        return sp.getInt(key, defValue);
    }

    /**
     * 保存对象
     *
     * @param key 键
     * @param obj 要保存的对象（Serializable的子类）
     * @param <T> 泛型定义
     */
    public static <T extends Serializable> void putObjectT(String key, T obj) {
        try {
            putObject(key, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param key 键
     * @param <T> 指定泛型
     * @return 泛型对象
     */
    public static <T extends Serializable> T getObjectT(String key) {
        try {
            return (T) getObject(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 存储对象
     */
    public static void putObject(String key, Object obj)
            throws IOException {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        putString(key, objectStr);
    }

    /**
     * 获取对象
     */
    public static Object getObject(String key) {
        String wordBase64 = getString(key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将byte数组转换成product对象
        Object obj = null;
        try {
            obj = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            bais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }



    public static void putUserBean(UserBean bean) {
        putObjectT(USER_Bean, bean);
    }
    public static UserBean getUser(){
        return getObjectT(USER_Bean);
    }
    public static void putRoleType(String roleType) {
        putObjectT("roleType", roleType);
    }

    public static String getRoleType() {
        return getObjectT("roleType");
    }
}
