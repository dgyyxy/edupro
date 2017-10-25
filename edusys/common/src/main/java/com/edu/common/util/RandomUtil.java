package com.edu.common.util;

import java.util.Random;

/**
 * Created by Gary on 2017/5/11.
 */
public class RandomUtil {

    public static int getRandNum(int min, int max) {
        int randNum = min + (int)(Math.random() * ((max - min) + 1));
        return randNum;
    }

    public static String getFourRandNum(){
        String result="";
        /*for(int i=0;i<4;i++){
            int intVal=(int)(Math.random()*26+97);
            result=result+(char)intVal;
        }*/
        int x;//定义两变量
        Random ne=new Random();//实例化一个random的对象ne
        x=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
        return result+x;
    }

    public static int getArrayRandItem(String[] ids){
        Random rand = new Random();
        int index = rand.nextInt(ids.length);
        return Integer.parseInt(ids[index]);
    }


    public static void main(String[] args) {
//        System.out.println(getSixRandNum());
        for(int i=0;i<1000;i++){
            System.out.println(getFourRandNum());
        }
        System.out.println("rlzp".equals("rlzp"));
    }
}
