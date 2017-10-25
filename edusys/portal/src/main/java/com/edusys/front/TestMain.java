package com.edusys.front;

import java.util.Random;

/**
 * Created by Gary on 2017/9/20.
 */
public class TestMain {
    public static void main(String[] args) {
        int x;//定义两变量
        Random ne=new Random();//实例化一个random的对象ne
        x=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
        System.out.println("产生的随机数是:"+x);//输出
    }
}
