package com.edu.common.util;

import java.text.NumberFormat;

/**
 * Created by Gary on 2017/6/7.
 */
public class NumberUtils {

    public static String getPercentStr(int num1, int num2){
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后0位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result + "%";
    }
}
