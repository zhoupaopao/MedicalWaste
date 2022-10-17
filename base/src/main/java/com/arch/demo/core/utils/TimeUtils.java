package com.arch.demo.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    /**
     * 时间和当前时间比较，并计算相差分钟
     *
     * @param tempTime
     * @return days相差天数
     */
    public static long diffTime2Now(String tempTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        // tempTime = sdf.format(tempTime);
        long days = 0;
        try {
            Date d1 = sdf.parse(tempTime);
            Date dnow = sdf.parse(nowTime);

            long diff = dnow.getTime()- d1.getTime() ;
            days = diff / (1000 * 60 );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;

    }
}
