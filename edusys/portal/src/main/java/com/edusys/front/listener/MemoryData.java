package com.edusys.front.listener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gary on 2017/9/20.
 */
public class MemoryData {
    private static Map<String, String> sessionIDMap = new HashMap<String, String>();

    public static Map<String, String> getSessionIDMap() {
        return sessionIDMap;
    }

    public static void setSessionIDMap(Map<String, String> sessionIDMap) {
        MemoryData.sessionIDMap = sessionIDMap;
    }

    public static void removeAll(){
        sessionIDMap = new HashMap<String, String>();
    }
}
