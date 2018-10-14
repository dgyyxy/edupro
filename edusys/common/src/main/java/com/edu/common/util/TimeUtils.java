package com.edu.common.util;

/**
 * Created by Gary.Duan on 2018/9/12.<br >
 */
public class TimeUtils {

    /**
     * 返回日时分秒
     * @param second
     * @return
     */
    public static String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days){
            return days + "天"+hours+"小时"+minutes+"分"+second+"秒";
        }else {
            return hours+"小时"+minutes+"分"+second+"秒";
        }
    }

    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatTime(long ms) {
        if(ms == 0){
            return "--";
        }
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        return sb.toString();
    }


}
