package com.easy.tech.serialutil.assist;

import static com.easy.tech.serialutil.configs.LogConfig.DEBUG;

import android.os.Debug;
import android.util.Log;

import com.easy.tech.serialutil.configs.LogConfig;

import java.util.Locale;

public class ConvertUtil {
    public static final String TAG = "ConvertUtil";

    //byte[]转换成HexString
    public static String bytesToHexString(byte[] src, int len){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || len <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase(Locale.ROOT);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        //if (stringBuilder.length() > 1) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase().replace(" ", "");
        if (DEBUG) Log.d(TAG, "hexString: {" + hexString  + "}, size: " + hexString.length());
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            if (DEBUG) Log.d(TAG, d[i] + " ");
        }
        return d;
    }

    //转换char to byte
    public static byte charToByte(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
}
