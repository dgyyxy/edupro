package com.edu.common.util;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by Gary on 2017/6/7.
 */
public class NumberUtils {

    public static String getPercentStr(int num1, int num2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后0位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result + "%";
    }

    public static String formatDouble(double value) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
         /*
          * setMinimumFractionDigits设置成2
          *
          * 如果不这么做，那么当value的值是100.00的时候返回100
          *
          * 而不是100.00
          */
//        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
         /*
          * 如果想输出的格式用逗号隔开，可以设置成true
          */
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

    public static void main(String[] args) {
        double num = 1.0233;
        System.out.println(formatDouble(num));

    }
}
