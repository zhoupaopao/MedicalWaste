package com.xinchen.medicalwaste.utils;

import android.os.Message;
import android.util.Log;

import java.math.BigDecimal;
import java.util.HashMap;

public class ScaleUtils {
    //称重传感器 置零指令 范围最大是10%
    //  const val COMMAND_ZERO = "006306016A"
    //4个传感器指令 标零的指令，没有限制
    public static String COMMAND_ZERO = "006306036C";
    //称重传感器 称重指令
    public static String COMMAND_SCALE = "000502050C";
    /**
     * @param hex 16进制字符串
     * @return 两两相加的和，16进制
     */
    private static String makeChecksum(String hex) {
        String hexdata = hex;
        if (hexdata == null || hexdata == "") {
            return "00";
        }
        hexdata = hexdata.replace(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total +=Integer.parseInt(s,16);
            num += 2;
        }

        return hexInt(total).toUpperCase();
    }

    /**
     * @param decimalNumber 十进制
     * @return 转化成的二进制字符串
     */
    private static String getBinaryNumber(int decimalNumber) {
//        int decimalNumber = decimalNumber;
        StringBuilder binaryStr = new StringBuilder();

        while (decimalNumber > 0) {
            int r = decimalNumber % 2;
            decimalNumber /= 2;
            binaryStr.append(r);
        }

        return binaryStr.reverse().toString();
    }

    /**
     * @param b 字节数组 ByteArray
     * @return 转化成的16进制字符串
     */
    public static String bytesToHexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);// 将高24位置0
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }


    /**
     * @param total 十进制数
     * @return 十进制转化成四位16进制
     */
    private static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        return  (a > 255)? (hexInt(a) + format(b)) : (format(a) + format(b));
    }


    /**
     * @param hex 16进制字符串
     * @return 将 16进制 hex,不够4位的高位补零
     */
    private static String format(int hex){
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0"+hexa;
        }
        return hexa;
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        val a= parseScale("01060206050003E8FF")
//        print(a)
//    }

    public static String parseScale(byte[]data){
        byte base = 0x30;
        if (data.length != 10) {
            Log.i("pqc", "接收数据长度异常。");
            return "0";
        }
        int x0 = (data[7] - base);//小数点位数
        int x5 = (data[6] - base) * 65536;
        int x4 = (data[5] - base) * 4096;
        int x3 = (data[4] - base) * 256;
        int x2 = (data[3] - base) * 16;
        int x1 = (data[2] - base) * 1;
        Log.i("pqc", "分重量：" + "t5=" + x5 + ",t4=" + x4 + ",t3=" + x3 + ",t2=" + x2 + ",t1=" + x1);
        Log.i("pqc", "小数位:" + (x0 & 3) + ",fendu:" + 1);
        double wieht = x5 + x4 + x3 + x2 + x1;
        wieht = (wieht) * 0.01;
        Log.i("pqc", "总重量=" + wieht);
        return String.format("%.2f", wieht) + "";
    }

    //重量解析
    public static String parseScale(String hex) {
        String weight = "0";

        if (hex.length() == 18) {
            HashMap<Character, Float> keyValue = new HashMap();
            keyValue.put('0',0.0001f);
            keyValue.put('1',0.0002f);
            keyValue.put('2',0.0005f);
            keyValue.put('3',0.001f);
            keyValue.put('4',0.002f);
            keyValue.put('5',0.005f);
            keyValue.put('6',0.01f);
            keyValue.put('7',0.02f);
            keyValue.put('8',0.05f);
            keyValue.put('9',0.1f);
            keyValue.put('A',0.2f);
            keyValue.put('B',0.5f);
            keyValue.put('C',1f);
            keyValue.put('D',2f);
            keyValue.put('E',5f);

            String addressHexString = hex.substring(0, 2);
            String commandCodeHexString = hex.substring(2, 4);
            String registerHexString = hex.substring(4, 6);
            String statusHexString = hex.substring(6, 8);
            String x4HexString = hex.substring(8, 10);

            String x4HexStringHigh = hex.substring(8, 9);//符号位
            String x4HexStringLow = hex.substring(9, 10);//

            String x3HexString = hex.substring(10, 12);
            String x2HexString = hex.substring(12, 14);
            String x1HexString = hex.substring(14, 16);

            String x321HexString = hex.substring(10, 16);

            String noLrcHexString = hex.substring(0, 16);

            String sumResult = makeChecksum(noLrcHexString);
            String checkLrc = sumResult.substring(sumResult.length() - 2).toUpperCase();

            String lrc = hex.substring(16, 18).toUpperCase();


//            Log.d(
//                "ScaleUtil", "addressHexString=${addressHexString}," +
//                        "commandCodeHexString=${commandCodeHexString}," +
//                        "registerHexString=${registerHexString}," +
//                        "statusHexString=${statusHexString}," +
//                        "x4HexString=${x4HexString}," +
//                        "x3HexString=${x3HexString}," +
//                        "x2HexString=${x2HexString}," +
//                        "x1HexString=${x1HexString}," +
//                        "x4HexStringHigh=${x4HexStringHigh}," +
//                        "x4HexStringLow=${x4HexStringLow}," +
//                        "checkLrc=${checkLrc}," +
//                        "lrc=${lrc}"
//            )

            if (checkLrc.equals(lrc)) {//检验
                switch (statusHexString) {//判断状态
                    case "06"://标定允许 稳定
                        //计算重量 X3 X2 X1 是分度数，X4 是分度值代号(最高位是负号)。 重量=分度数*分度值
                        float number = keyValue.get(x4HexStringLow.charAt(0)); //分度数
                        //val valueHex = makeChecksum(x321HexString)
                        int value=Integer.parseInt(x321HexString,16);
                        // val value: Float = Integer.parseInt(valueHex, 16).toFloat()//分度值

                        String signBinaryString = getBinaryNumber(
                                Integer.parseInt(
                                        x4HexStringHigh,
                                        16
                                )
                        );
                        //正负符号
                        float sign =(signBinaryString.isEmpty() || signBinaryString.charAt(0) == '0')? 1f : -1f;


                        //四舍五入取三位小数
                        weight= String.format("%.3f",number*(value * sign));
                        BigDecimal bigDecimal=new BigDecimal(weight);
                        weight=bigDecimal.stripTrailingZeros().toPlainString();

//                        Log.d(
//                            "ScaleUtil",
//                            "number=${number},value=${value},signBinaryString=${signBinaryString}"
//                        )


                    break;

                    case "07": //标定允许 稳定 零位
                        weight = "0";
                    break;


                }
            } else {
//                Log.d(
//                    "ScaleUtil",
//                    "lrc校验失败,lrc=${lrc},checkLrc=${checkLrc}"
//                )
            }


        }

        return weight;
    }


    /**
     * 将十六进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}