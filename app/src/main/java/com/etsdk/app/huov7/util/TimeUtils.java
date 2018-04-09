package com.etsdk.app.huov7.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018\3\8 0008.
 */

public class TimeUtils {
    public static String getTime(long time) {
        String timeString = "";
        long current_time = System.currentTimeMillis();
        long sub_time = (current_time - time) / 1000 + 20;
        if (sub_time < 60) {
            timeString = sub_time + "秒前";
        } else if (sub_time < 3600) {
            timeString = (sub_time / 60) % 60 + "分前";
        } else if (sub_time < 3600 * 24) {
            timeString = (sub_time / 3600) % 24 + "小时前";
        } else {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            timeString = dateFormat.format(date);
            try {
                Date date2 = dateFormat.parse(timeString);
                current_time = date2.getTime() + 24 * 3600 * 1000;
                sub_time = (current_time - time * 1000) / 1000;
                if (sub_time < 3600 * 24 * 2) {
                    timeString = "昨天";
                } else if (sub_time < 3600 * 24 * 3) {
                    timeString = "前天";
                } else if (sub_time < 3600 * 24 * 11) {
                    timeString = ((sub_time / 3600 / 24)) % 11 + "天前";
                }else {
                    timeString  = dateFormat.format(new Date(time * 1000));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeString;
    }
}
