package com.edusys.manager.dao;

import com.edu.common.util.MybatisGeneratorUtil;
import com.edu.common.util.PropertiesFileUtil;

/**
 * 代码生成类
 * Created by Gary on 2017/3/27.
 */
public class Generator {

    //根据命名规范，只修改此常量值即可
    private static String MODULE = "manager";
    private static String DATABASE = "edusys";
    private static String TABLE_PREFIX = "edu";
    private static String PACKAGE_NAME = "com.edusys.manager";

    private static String JDBC_DRIVER = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.driver");
    private static String JDBC_URL = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.url");
    private static String JDBC_USERNAME = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.username");
    private static String JDBC_PASSWORD = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.password");

    /**
     * 自动代码生成
     * @param args
     */
    public static void main(String[] args) throws Exception {
        MybatisGeneratorUtil.generator(JDBC_DRIVER, JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD, MODULE, DATABASE, TABLE_PREFIX, PACKAGE_NAME);
    }
}
