package com.arch.demo.core.utils;

import android.app.Application;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecodeUtils {
    public static final String APP_ID = "6321123456789987";
    public static final String strDefaultKey = "uhEAnqbJ^f<I#mqz";
    public static final String PUBLIC_KEY_RSA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUvTy//9E+6P+EJr6zpN+F7XFyZ0I7ghPb0s4sGqpzK5Nkm+h1pLLnzY2QmtseL7QTyK6jQAZmZCMAkAlCo0uDYUP+dDZF9BRX+vNKRuEZvQI5BNE9eKCIXLspx0jGcj4wt0cDAiokguPJlGQ2ct725Xi7ghvtGVrkSlu/ngeSNQIDAQAB";


    //AES加密
    public static String encrypt(String content) {
        Log.i("pqc","加密前："+content);
        try {
            byte[] raw = strDefaultKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));

//            byte[] encode = Base64.encode(encrypted, Base64.DEFAULT);

//            String s = new String(encode);

            return bytesToHexString(encrypted);
//            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ////AES解密
    public static String decrypt(String content) {
        try {
            byte[] raw = strDefaultKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hexStringToByteArray(content);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 32位MD5加密
     *
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 16位MD5加密
     * 实际是截取的32位加密结果的中间部分(8-24位)
     *
     * @param content
     * @return
     */
    public static String md5Decode16(String content) {
        return md5Decode32(content).substring(8, 24);
    }

}
